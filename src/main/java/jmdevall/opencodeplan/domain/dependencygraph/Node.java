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
	public String content;
	public List<Node> children;
	
	public void debugRecursive(int level) {
    	printlevel(level);
    	if(this.getChildren().isEmpty()) {
        	System.out.println(String.format("%s %s: %s , %s"
        			,this.getType(), id.getFile()
        			,this.id.getRange().toString()
        			,this.getContent()));
    	}
    	else {
        	System.out.println(String.format("%s %s: %s"
        			,this.getType(), id.getFile()
        			,this.id.getRange().toString()));
        	System.out.println(this.getContent());
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
    
/*
    public String prompt() {
    	this.getContent();
    	
    }*/
}
