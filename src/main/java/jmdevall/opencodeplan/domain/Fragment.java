package jmdevall.opencodeplan.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
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
	private Node node;
	
	
	public static Fragment newFragment(Node cu, Node block) {
		return Fragment.builder()
				.node(extractCodeFragment(cu,block))
				.build();
	}
	
	public static Node extractCodeFragment(Node root, Node block) {
	    
	    Stream<Node> consideredChildren;
	    
	    if(root.getType().equals("MethodDeclaration") && !root.containsByPosition(block)) {
	    	consideredChildren=root.getChildren()
	    			.stream().filter(
	    					(c)->!c.getType().equals("BlockStmt"));
	    }else {
	    	consideredChildren=root.getChildren().stream();
	    }
	    
	    List<Node> prunedChildren=consideredChildren
	    		.map(c->extractCodeFragment(c,block))
	    		.collect(Collectors.toList());
	
	    return Node.builder()
	    		.id(root.getId())
	    		.type(root.getType())
	    		.content(root.getContent())
	    		.children(prunedChildren)
	    		.build();
	
	}
	

}