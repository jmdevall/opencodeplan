package jmdevall.opencodeplan.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Chunk;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.plangraph.CMI;
import jmdevall.opencodeplan.domain.plangraph.ChangeType;
import jmdevall.opencodeplan.domain.promptmaker.DiffUtil;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class Fragment {

	/**
	Simply extracting
	code of the block 𝐵 loses information about relationship of 𝐵 with the surrounding code.
	Keeping the entire file on the other hand takes up prompt space and is often unnecessary.
	We found the surrounding context is most helpful when a block belongs to a class. For such
	blocks, we sketch the enclosing class. That is, in addition to the code of block 𝐵, we also
	keep declarations of the enclosing class and its members. As we discuss later, this sketched
	representation also helps us merge the LLM’s output into a source code file more easily.
	*/
	private Node prunedcu;
	private Node originalcu;
	private Node revised;
	
	/**
	 * Fragment is like a copy of the Compilation Unit node but some of the child nodes that represent the method blocks has been
	 * replaced with empty blocks because it is not of interest.
	 * 
	 * @param cu
	 * @param principalMethodBlock
	 * @return
	 */
	public static Fragment newFromPrunedCuNode(Node originalcu, NodeId affectedSubNode) {
		return Fragment.builder()
				.originalcu(originalcu)
				.prunedcu(Fragment.extractCodeFragment(originalcu,Arrays.asList(affectedSubNode),null))
				.build();
	}
	
	public static Fragment newFromCuNode(Node cu) {
		return Fragment.builder()
				.originalcu(cu)
				.prunedcu(cu)
				.build();
	}
	

	public static Node extractCodeFragment(Node root, List<NodeId> affectedBlocks, Node parent) {
	    
	    Stream<Node> consideredChildren=root.getChildren().stream();
	    
	    //other methods different to the affected: replace blockStmt with other empty "SkipBlock"
	    if(root.isMethodDeclaration() && !root.getId().containsByPosition(
	    		affectedBlocks.stream()
	    		.collect(Collectors.toList()))
	    ) {
	    	consideredChildren=consideredChildren
					.map(c->{
							return (c.getType().equals("BlockStmt"))?
								Fragment.skipedNode(c):c;
					});
	    }
	    
	    Node newNode=root.newCopyWithoutChildren();
	    
	    List<Node> prunedChildren=consideredChildren
	    		.map(c->extractCodeFragment(c,affectedBlocks,newNode))
	    		.collect(Collectors.toList());
	
	    newNode.setChildren(prunedChildren);
	    
	    return newNode;
	}

	public static Node skipedNode(Node c) {
		return Node.builder()
		.id(c.getId())
		.type("SkipedBlockFragment")
		.parent(c.parent)
		.children(Collections.emptyList())
		.rrange(c.getRrange())
		.content("")
		.original(c)
		.build();
	}
	

	public String merge(String llmrevised){
		List<String> original= DiffUtil.tolines(originalcu.prompt());
		List<String> pruned = DiffUtil.tolines(prunedcu.prompt());
		List<String> revised= DiffUtil.tolines(llmrevised);
		
		
		//patchPrunedToOriginal should only have deltas of source=1 line and target= multiples lines because It's only method body deletions
		Patch<String> patchPrunedToOriginal=DiffUtils.diff(pruned, original);
		
		Patch<String> patchPrunedToRevised=DiffUtils.diff(pruned, revised);
		
		ArrayList<String> prunedCopy=new ArrayList<String>(pruned);
		for(AbstractDelta<String> delta:patchPrunedToOriginal.getDeltas()) {

			Chunk<String> source=delta.getSource();
			int position=source.getPosition();
			
			//in this line it actually goes more than one line but do not alter the positions for the next patch 
			String multilineHack=delta.getTarget().getLines().stream().collect(Collectors.joining(System.lineSeparator()));
			prunedCopy.remove(position);
			prunedCopy.add(position, multilineHack);
		}
		
		try {
			List<String> finalpatch = DiffUtils.patch(prunedCopy,patchPrunedToRevised);
			return finalpatch.stream().collect(Collectors.joining(System.lineSeparator()));
		} catch (PatchFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}
	
	

	
	public void setRevised(Node newFragment){
		this.revised=newFragment;
	}

	
	public List<CMI> classifyChanges() {
		List<String> original= DiffUtil.tolines(originalcu.prompt());
		List<String> revisedCode = DiffUtil.tolines(revised.prompt());
		
		Patch<String> originalToRevised=DiffUtils.diff(original, revisedCode);
		
		for(AbstractDelta<String> delta:originalToRevised.getDeltas()) {

			Chunk<String> source=delta.getSource();
			Chunk<String> target=delta.getTarget();
			
			int sourceLine=source.getPosition()+1;
			int targetLine=target.getPosition()+1;
			
			ChangeType ct=fromDeltaType(delta.getType());
			
			//con esto filtraríamos los nodos raiz de algun tipoTag que se han añadido en revised
			if(ct==ChangeType.ADD) {
				revised.toStream()
					.filter(n -> n.getId().getRange().getBegin().getLine()==sourceLine)
					.filter(n -> n.getNodeTypeTag()!=null)
					.filter(n -> n.getParent()!=null && n.getNodeTypeTag()!=n.getParent().getNodeTypeTag())
					.collect(Collectors.toList());
			}
					
		}

		//TODO:
		return null;
 
	}
	
	private ChangeType fromDeltaType(DeltaType deltaType) {
		if(deltaType==DeltaType.INSERT) {
			return ChangeType.ADD;
		}
		if(deltaType==DeltaType.DELETE) {
			return ChangeType.DELETION;
		}
		if(deltaType==DeltaType.CHANGE) {
			return ChangeType.MODIFICATION;
		}
		throw new IllegalArgumentException();
	}
	
}