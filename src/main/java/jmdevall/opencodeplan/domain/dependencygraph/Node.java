package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Builder(toBuilder=true)
@Getter
@Slf4j
public class Node {
    private NodeId id;
    
	public String type;
	public Node parent;
	public List<Node> children;
	
	private Rrange rrange;
	private String content;
	
	public Node newCopyWithoutChildren() {
		return Node.builder()
		.id(this.getId())
		.type(this.getType())
		.parent(this.getParent())
		.rrange(this.getRrange())
		.content(this.getContent())
		.build();
	}
	
	public void debugRecursive(int level) {
    	printlevel(level);
    	if(this.getChildren().isEmpty()) {
        	System.out.println(String.format("[%s]+[%s]: A%s R%s, \n[%s]"
        			,this.getType(), id.getFile()
        			,this.id.getRange().toString()
        			,this.rrange.toString()
        			,this.getContent()));
    	}
    	else {
        	System.out.println(String.format("[%s]+[%s]: A%s R[%s], \n[%s]"
        			,this.getType(), id.getFile()
        			,this.id.getRange().toString()
        			,this.rrange.toString()
        	        ,this.getContent()));
    	}
    	
    	
    	for(Node child:this.getChildren()) {
    		child.debugRecursive(level+1);
    	}
    }
	
	public Node getRootParent() {
		if(this.parent==null) {
			return this;
		}
		else {
			return this.parent.getRootParent();
		}
	}
	
    private void printlevel(int level) {
    	for(int i=0;i<level;i++) {
    		System.out.print("-");
    	}
    }
	
	public boolean containsByPosition(Node other) {
		return this.id.isSameFile(other.getId()) 
				&& 
				this.id.getRange().contains(other.id.getRange());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return Objects.equals(id, other.id);
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}
    
	//TODO: no se usa
	public Rrange relativeRange() {
		if(parent==null) {
			return this.rrange;
		}
		else { 
			Rrange arangeparent=parent.rrange;
			return rrange.minus(arangeparent);
		}
	}

	public String prompt() {
		return this.prompt(new StringBuffer());
	}
	
    public String prompt(StringBuffer sb) {
    
    	if(rrange.getBegin()>sb.length()) {
    		System.out.println("mal");
    	}
    	sb.replace(rrange.getBegin(), rrange.getEnd(), content);

    	for( Node child:this.children ) {
    		child.prompt(sb);
    	}
    	
    	return sb.toString();
    }

    public Stream<Node> toStream(){
    	java.util.stream.Stream.Builder<Node> s=Stream.<Node>builder();
    	addRecursive(s);
    	return s.build();
    }
    
	private void addRecursive(java.util.stream.Stream.Builder<Node> s) {
		s.add(this);
		for(Node child:children){
			child.addRecursive(s);
		}
	}
	
	public boolean isMethodContaining(Node other){
		return this.isMethodDeclaration() && this.containsByPosition(other);
	}
	
	public boolean containsByPosition(List<Node> blocks){
		for(Node node:blocks) {
			if(this.containsByPosition(node)) {
				return true;
			}
		}
		return false;
	}

	public static Node extractCodeFragment(Node root, List<Node> affectedBlocks, Node parent) {
	    
	    Stream<Node> consideredChildren=root.getChildren().stream();
	    
	    //otros métodos diferentes al afectado: se sustituye el BlockStmt por un nodo vacio
	    if(root.isMethodDeclaration() && !root.containsByPosition(affectedBlocks)) {
	    	consideredChildren=consideredChildren
					.map(c->{
							return (c.getType().equals("BlockStmt"))?
								skipedNode(c):c;
					});
	    }
	    
	    Node newNode=root.newCopyWithoutChildren();
	    
	    List<Node> prunedChildren=consideredChildren
	    		.map(c->extractCodeFragment(c,affectedBlocks,newNode))
	    		.collect(Collectors.toList());
	
	    newNode.setChildren(prunedChildren);
	    
	    return newNode;
	}

	private boolean isMethodDeclaration() {
		return this.type.equals("MethodDeclaration");
	}
	

	private static Node skipedNode(Node c) {
		return builder()
		.id(c.getId())
		.type("SkipedBlockFragment")
		.parent(c.parent)
		.children(Collections.emptyList())
		.rrange(c.getRrange())
		.content("")
		.build();
	}
}
