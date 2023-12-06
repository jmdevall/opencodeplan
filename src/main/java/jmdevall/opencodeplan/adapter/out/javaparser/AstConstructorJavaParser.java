package jmdevall.opencodeplan.adapter.out.javaparser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.dependencygraph.LineColRange;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.cusource.CuSourceFixBug;
import jmdevall.opencodeplan.domain.dependencygraph.IndexPosRange;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AstConstructorJavaParser implements CuProcessor{
	private HashMap<String,Node> forest;
	private CuSource cuSource;
	
	public AstConstructorJavaParser(CuSource cuSource) {
		forest=new HashMap<String,Node>();
		this.cuSource=new CuSourceFixBug(cuSource);
	}

	private jmdevall.opencodeplan.domain.dependencygraph.Node toDomainNode(com.github.javaparser.ast.Node node){
    	Node domainNode = toDomainNode(node,null);
    	fix(domainNode);
		return domainNode;
    }
	

	private jmdevall.opencodeplan.domain.dependencygraph.Node toDomainNode(
			com.github.javaparser.ast.Node node
			,jmdevall.opencodeplan.domain.dependencygraph.Node parent) {
		
		
		NodeId nodeId = Util.toNodeId(node);
		
		String cucontent=this.cuSource.getSource(nodeId.getFile());
		
		LineColRange range = nodeId.getRange();
		IndexPosRange rrange=absoluterange(range, cucontent);

		
		String nodecontent;
		try {
			nodecontent = cucontent.substring(rrange.getBegin(), rrange.getEnd());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			nodecontent="";
		}

		Node domainNode=Node.builder()
    		.id(nodeId)
    		.type(node.getMetaModel().getTypeName())
    		.content(nodecontent)
    		.parent(parent)
    		.rrange(rrange)
    	    .build();

		ArrayList<Node> children=new ArrayList<Node>();
    	if(isInterestedType(domainNode.getType())) {
            for(com.github.javaparser.ast.Node child: node.getChildNodes()) {
            	Node childomain=toDomainNode(child,domainNode);
                children.add(childomain);
            }
    	}
       
        domainNode.setChildren(children);
        return domainNode;
    }
	
	private String notInterestedTypes[]= {"FieldDeclaration","LambdaExpr"};
	
	private boolean isInterestedType(String type) {
		for(String nitype: notInterestedTypes) {
			if(nitype.equals(type)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * TODO: por lo que he visto en algunos casos javaparser crea nodos hijos tal que el padre están fuera del padre.
	 * Esta funcion trataría de arreglarlo. moviendo el hijo como si fuera un hijo del abuelo.
	 * 
	 * De todas formas quizas no haga falta. Creo que solo ocurre en los nodos VariableDeclaration y FieldDeclaration y no no hace falta llegar hasta ese nivel
	 * 
	 * Al final lo que vale son las relaciones de dependencias entre métodos. Da igual entrar al detalle de a que sentencia dentro del método es la que está relacionada. 
	 *  
	 * @param node
	 */
	public void fix(Node node) {
		Node parent=node.parent;
		
		if(parent!=null) {
			Node grandparent=parent.parent;
			if(grandparent!=null &&	!parent.getId().containsByPosition(node.getId())) {
					
				List<Node> brothers=parent.getChildren();
				List<Node> exbrothers=brothers.stream()
						.filter(n->!n.getId().equals(node.getId()))
						.collect(Collectors.toList());
				parent.setChildren(exbrothers);
			}
		}
		for(Node n:node.children) {
			fix(n);
		}
	}

	private IndexPosRange absoluterange(LineColRange range, String cucontent) {

		return new IndexPosRange(
				range.getBegin().absolute(cucontent),
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
