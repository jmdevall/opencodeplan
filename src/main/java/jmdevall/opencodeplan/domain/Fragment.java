package jmdevall.opencodeplan.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

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
	

	public void merge(String llmContentRevised){
		List<String> prunedlines = DiffUtil.tolines(prunedcu.prompt());
		Patch<String> prunedpatch=DiffUtils.diff(
				prunedlines,
				DiffUtil.tolines(originalcu.prompt())
		);
		
		Patch<String> llmpatch=DiffUtils.diff(
				prunedlines,
				DiffUtil.tolines(llmContentRevised)
		);
		
	}
	
}