package jmdevall.opencodeplan.adapter.out.javaparser;


import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.CompilationUnit;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.dependencygraph.Position;
import jmdevall.opencodeplan.domain.dependencygraph.Range;
import jmdevall.opencodeplan.domain.dependencygraph.Rrange;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AstConstructorJavaParser implements CuProcessor{
	private HashMap<String,Node> forest;
	private CuSource cuSource;
	
	public AstConstructorJavaParser(CuSource cuSource) {
		forest=new HashMap<String,Node>();
		this.cuSource=cuSource;
	}

	private jmdevall.opencodeplan.domain.dependencygraph.Node toDomainNode(com.github.javaparser.ast.Node node){
    	return toDomainNode(node,null);
    }
	public static String removeLastChar(String s) {
	    return (s == null || s.length() == 0)
	      ? null 
	      : (s.substring(0, s.length() - 1));
	}

	private jmdevall.opencodeplan.domain.dependencygraph.Node toDomainNode(
			com.github.javaparser.ast.Node node
			,jmdevall.opencodeplan.domain.dependencygraph.Node parent) {
		
		//String content = node.toString();
		//String cucontent = node.findCompilationUnit().get().toString();
		
		NodeId nodeId = Util.toNodeId(node);
		
		String cucontent=cuSource.getSource(nodeId.getFile());
		
		Range range = nodeId.getRange();
		Rrange rrange=absoluterange(range,cucontent);
		

		String nodecontent = cucontent.substring(rrange.getBegin(), rrange.getEnd());
		
		/*if(!assertEquals(substring,node.toString(),""+nodeId.getRange()+" cu=["+cucontent+"]")) {
			int food=3;
			System.out.println("node."+node.getMetaModel().getTypeName());
		}*/

		if(parent!=null) {
			Rrange arangeparent=absoluterange(parent.getId().getRange(),cucontent);
			rrange=rrange.minus(arangeparent);
		}
		
		Node domainNode=Node.builder()
    		.id(nodeId)
    		.type(node.getMetaModel().getTypeName())
    		.content(nodecontent)
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

	private boolean assertEquals(String expected, String actual,String extra) {
		if(!expected.equals(actual)) {
			log.error(String.format("no coinciden cadenas: [%s] [%s] [%s])",expected, actual,extra));
			return false;
		}
		return true;
		
	}
	
	private Rrange absoluteRange2(Position begin, int nodelength, String cucontent) {
		int posini=begin.absolute(cucontent);
		return new Rrange(posini,posini+nodelength);
	}

	// otra forma de calculo pero como el rango en javaparser no va bien, mejor no usar esta forma
	@Deprecated
	private Rrange absoluterange(Range range, String cucontent) {

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
