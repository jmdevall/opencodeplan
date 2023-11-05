package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyLabel;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;

public class ChildParentRelFinder extends VoidVisitorAdapter<List<DependencyRelation>>{
	
	
	public ChildParentRelFinder() {
		super();
	}


	@Override
	public void visit(MethodDeclaration n, List<DependencyRelation> rels) {
		super.visit(n, rels);
		
		Optional<com.github.javaparser.ast.Node> parent=n.getParentNode();
		if(parent.isPresent()) {
			DependencyRelation childToParent=DependencyRelation.builder()
			.origin(Util.toNodeId(n))
			.destiny(Util.toNodeId(n.getParentNode().get()))
			.label(DependencyLabel.CHILD_OF)
			.build();
			
			DependencyRelation parentToChild=DependencyRelation.builder()
			.origin(Util.toNodeId(n.getParentNode().get()))
			.destiny(Util.toNodeId(n))
			.label(DependencyLabel.PARENT_OF)
			.build();

			rels.addAll(Arrays.asList(childToParent,parentToChild));
		}
	}
}