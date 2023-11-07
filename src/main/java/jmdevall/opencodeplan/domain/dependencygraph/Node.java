package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
	
	private IndexPosRange rrange;
	private String content;
	
	//skiped nodes keep the original node
	public Node original;
	
	public Node newCopyWithoutChildren() {
		return Node.builder()
		.id(this.getId())
		.type(this.getType())
		.parent(this.getParent())
		.rrange(this.getRrange())
		.content(this.getContent())
		.original(this.original)
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
    
	public IndexPosRange relativeRange() {
		if(parent==null) {
			return this.rrange;
		}
		else { 
			IndexPosRange arangeparent=parent.rrange;
			return rrange.minus(arangeparent);
		}
	}
	
	public String prompt() {
		return this.prompt(new StringBuffer(this.content));
	}
	
    
    public String prompt(StringBuffer sbpadre) {
    	StringBuffer sbyo = new StringBuffer(content);
    	
    	//es necesario ordenador los hijos y hacer las sustituciones desde el final hasta el principio para que no se descuadre.
    	ArrayList<Node> consideredChildren=new ArrayList<Node>(this.children);
    	consideredChildren.sort((o1, o2) -> o2.relativeRange().getBegin() - o1.relativeRange().getBegin());
    	
    	for(Node child:consideredChildren) {
       		child.prompt(sbyo);
    	}
    	IndexPosRange relativeRange = this.relativeRange();
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

	public boolean isMethodDeclaration() {
		return this.type.equals("MethodDeclaration");
	}
}
