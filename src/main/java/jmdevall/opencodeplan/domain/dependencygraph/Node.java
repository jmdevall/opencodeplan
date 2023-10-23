package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder=true)
@Getter
public class Node {
    private NodeId id;
    
	public String type;
	public Node parent;
	public List<Node> children;
	private String content;
	private Arange arange=null;
	
	public Arange getArange(){
		if(this.arange==null) {
			arange=new Arange(
					this.range.getBegin().absolute(content),
					this.range.getEnd().absolute(content));
		}
		return this.arange;
	}
	
	
	public void debugRecursive(int level) {
    	printlevel(level);
    	if(this.getChildren().isEmpty()) {
        	System.out.println(String.format("[%s]+[%s]: A%s , \n%s"
        			,this.getType(), id.getFile()
        			,this.id.getRange().toString()
        			,this.getContent()));
    	}
    	else {
        	System.out.println(String.format("[%s]+[%s]: A%s , "
        			,this.getType(), id.getFile()
        			,this.id.getRange().toString()
        	,this.getContent()));
    	}
    	
    	
    	for(Node child:this.getChildren()) {
    		child.debugRecursive(level+1);
    	}
    }
	
	public boolean containsByPosition(Node other) {
		return this.id.getRange().contains(other.id.getRange());
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
    


/*
    public String prompt() {
    	
    	StringBuilder original = new StringBuilder(this.getContent());
    	for(Node child:this.children) {
    		int from=convertirLineaColumnaAIndice(content, child.getId().getRelative().getBegin().getLine(), child.getId().getRelative().getBegin().getColumn());
    		int to=convertirLineaColumnaAIndice(content, child.getId().getRelative().getEnd().getLine(), child.getId().getRelative().getEnd().getColumn());
    		original.replace(from, to, child.prompt());
    	}
    	return original.toString();

    }
    */
}
