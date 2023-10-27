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
	private String content;
	private Rrange rrange;
	
	
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
    


    public String prompt(String cucontent) {
  
    	log.debug(this.getType()+" ["+this.getContent()+"] "+this.getContent().length());
    	StringBuilder original = new StringBuilder(cucontent);
    	
    	
    	for(Node child:this.children) {
       		String childprompt = child.prompt();
       		log.info("childprompt="+childprompt);
			original.replace(child.getRrange().getBegin(), child.getRrange().getEnd(), childprompt);
    	}
    	String string = original.toString();
    	
    	log.info("sustituido="+string);
		return string;

    }
    
}
