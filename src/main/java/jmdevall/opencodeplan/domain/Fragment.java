package jmdevall.opencodeplan.domain;

import jmdevall.opencodeplan.domain.dependencygraph.Node;

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