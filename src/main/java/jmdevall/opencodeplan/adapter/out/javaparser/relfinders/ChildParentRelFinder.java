package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.Label;
import jmdevall.opencodeplan.domain.Rel;

public class ChildParentRelFinder extends VoidVisitorAdapter<List<Rel>>{
	
	
	public ChildParentRelFinder() {
		super();
	}


	@Override
	public void visit(MethodDeclaration n, List<Rel> rels) {
		super.visit(n, rels);
		
		Optional<com.github.javaparser.ast.Node> parent=n.getParentNode();
		if(parent.isPresent()) {
			Rel childToParent=Rel.builder()
			.origin(Util.toNodeId(n))
			.destiny(Util.toNodeId(n.getParentNode().get()))
			.label(Label.CHILD_OF)
			.build();
			
			Rel parentToChild=Rel.builder()
			.origin(Util.toNodeId(n.getParentNode().get()))
			.destiny(Util.toNodeId(n))
			.label(Label.PARENT_OF)
			.build();

			rels.addAll(Arrays.asList(childToParent,parentToChild));
		}
	}
}