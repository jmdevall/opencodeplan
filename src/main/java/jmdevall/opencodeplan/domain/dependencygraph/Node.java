package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.List;
import java.util.Objects;

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
	public Node extractCodeFragment(Node root, Node blockB) {
	    // Crear un nuevo nodo que representará el fragmento de código
	    Node codeFragment = new Node();
	    codeFragment.type = root.type;
	    codeFragment.content = root.content;
	    codeFragment.children = new ArrayList<>();

	    // Recorrer el árbol AST y procesar cada nodo
	    for (Node child : root.children) {
	        if (child.equals(blockB)) {
	            // Si el nodo es el bloque de código B, agregarlo al fragmento de código
	            codeFragment.children.add(child);
	        } else {
	            // Si el nodo no es el bloque de código B, "plegar" el subárbol
	            Node foldedSubtree = extractCodeFragment(child, blockB);
	            if (foldedSubtree != null) {
	                codeFragment.children.add(foldedSubtree);
	            }
	        }
	    }

	    return codeFragment;
	}
    */
    
}
