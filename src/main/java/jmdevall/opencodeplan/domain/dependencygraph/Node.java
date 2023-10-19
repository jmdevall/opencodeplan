package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Node {
    private NodeId id;
    
	public String type;
	public String content;
	public List<Node> children;
	
	public void debugRecursive(int level) {
    	printlevel(level);
    	if(this.getChildren().isEmpty()) {
        	System.out.println(String.format("%s %s: [%d,%d]->[%d,%d] , %s"
        			,this.getType(), id.getFile()
        			,id.getBegin().getLine(),id.getBegin().getColumn()
        			,id.getEnd().getLine(),id.getEnd().getColumn()
        			,this.getContent()));
    	}
    	else {
        	System.out.println(String.format("%s %s: [%d,%d]->[%d,%d]"
        			,this.getType(), id.getFile()
        			,id.getBegin().getLine(),id.getBegin().getColumn()
        			,id.getEnd().getLine(),id.getEnd().getColumn()));
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
    
}
