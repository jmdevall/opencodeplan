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
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
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
	
	private String revised;

	public void merge(String newFragment){
		List<String> original= DiffUtil.tolines(originalcu.prompt());
		List<String> pruned = DiffUtil.tolines(prunedcu.prompt());
		List<String> revised= DiffUtil.tolines(newFragment);
		
		
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
			this.revised=finalpatch.stream().collect(Collectors.joining(System.lineSeparator()));
		} catch (PatchFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}