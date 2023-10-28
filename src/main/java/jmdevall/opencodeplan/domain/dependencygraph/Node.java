package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.List;
import java.util.Objects;

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
	
    private void printlevel(int level) {
    	for(int i=0;i<level;i++) {
    		System.out.print("-");
    	}
    }
	
	public boolean containsByPosition(Node other) {
		return this.id.getRange().contains(other.id.getRange());
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
    
    	sb.replace(rrange.getBegin(), rrange.getEnd(), content);

    	for( Node child:this.children ) {
    		child.prompt(sb);
    	}
    	
    	return sb.toString();
    }
    
    
}
