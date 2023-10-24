package jmdevall.opencodeplan.adapter.out.javaparser;


import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.CompilationUnit;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.dependencygraph.Range;
import jmdevall.opencodeplan.domain.dependencygraph.Rrange;
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


	private jmdevall.opencodeplan.domain.dependencygraph.Node toDomainNode(
			com.github.javaparser.ast.Node node
			,jmdevall.opencodeplan.domain.dependencygraph.Node parent) {
		String content = node.toString();

		String cucontent=node.findCompilationUnit().get().toString();
		
		
		NodeId nodeId = Util.toNodeId(node);
		Rrange rrange;
		
		Range range = nodeId.getRange();
		rrange=absoluterrange(range, cucontent);
		int cosa=cucontent.length();
		assertEquals(cucontent.substring(rrange.getBegin(), rrange.getEnd()),node.toString());

		if(parent!=null) {
			Rrange arangeparent=absoluterrange(parent.getId().getRange(),cucontent);
			rrange=rrange.minus(arangeparent);
		}
		
		Node domainNode=Node.builder()
    		.id(nodeId)
    		.type(node.getMetaModel().getTypeName())
    		.content(content)
    		.parent(parent)
    		.rrange(rrange)
    	    .build();

		ArrayList<Node> children=new ArrayList<Node>();
    	
        for(com.github.javaparser.ast.Node child: node.getChildNodes()) {
        	Node childomain=toDomainNode(child,domainNode);
            children.add(childomain);
        }
       
        domainNode.setChildren(children);
        return domainNode;
    }

	private void assertEquals(String substring, String string) {
		if(!substring.equals(string)) {
			log.error("no coinciden cadenas)");
		}
		
	}

	private Rrange absoluterrange(Range range, String cucontent) {
		return new Rrange(range.getBegin().absolute(cucontent),
				          range.getEnd().absolute(cucontent)+1);
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
