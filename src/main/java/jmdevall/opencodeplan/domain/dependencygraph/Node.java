package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.ArrayList;
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
		return this.prompt2(new StringBuffer(this.content));
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
    
    public String prompt2(StringBuffer sbpadre) {
    	StringBuffer sbyo = new StringBuffer(content);
    	
    	//es necesario ordenador los hijos y hacer las sustituciones desde el final hasta el principio para que no se descuadre.
    	ArrayList<Node> consideredChildren=new ArrayList<Node>(this.children);
    	consideredChildren.sort((o1, o2) -> o2.relativeRange().getBegin() - o1.relativeRange().getBegin());
    	
    	for(Node child:consideredChildren) {
       		child.prompt2(sbyo);
    	}
    	Rrange relativeRange = this.relativeRange();
		sbpadre.replace(relativeRange.getBegin(), relativeRange.getEnd(), sbyo.toString());
    	return sbpadre.toString();

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
		return this.isMethodDeclaration() && this.id.containsByPosition(other.getId());
	}
	


	public static Node extractCodeFragment(Node root, List<Node> affectedBlocks, Node parent) {
	    
	    Stream<Node> consideredChildren=root.getChildren().stream();
	    
	    //otros métodos diferentes al afectado: se sustituye el BlockStmt por un nodo vacio
	    if(root.isMethodDeclaration() && !root.getId().containsByPosition(affectedBlocks.stream().map(n->n.getId()).collect(Collectors.toList()))) {
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
