package jmdevall.opencodeplan.adapter.out.javaparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.CompilationUnit;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AstConstructorJavaParser implements CuProcessor{
	private HashMap<String,Node> forest;
	
	public AstConstructorJavaParser() {
		forest=new HashMap<String,Node>();
	}

	private jmdevall.opencodeplan.domain.dependencygraph.Node toDomainNode(com.github.javaparser.ast.Node node){
    	return toDomainNode(node,null);
    }

	private jmdevall.opencodeplan.domain.dependencygraph.Node toDomainNode(com.github.javaparser.ast.Node node,jmdevall.opencodeplan.domain.dependencygraph.Node parent) {

        Node domainNode=Node.builder()
    		.id(Util.toNodeId(node))
    		.type(node.getMetaModel().getTypeName())
    		.content(node.toString())
    		.parent(parent)
    	    .build();

		ArrayList<Node> children=new ArrayList<Node>();
    	
        for(com.github.javaparser.ast.Node child: node.getChildNodes()) {
        	Node childomain=toDomainNode(child);
            children.add(childomain);
        }
       
        domainNode.setChildren(children);
        return domainNode;
    }

	@Override
	public void process(CompilationUnit cu) {
		Node domainNode=toDomainNode(cu);
		forest.put(Util.getFileNameOfCompilationUnit(cu), domainNode);
	}

	public HashMap<String, Node> getForest() {
		return forest;
	}

}
