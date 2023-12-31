package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyLabel;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;
import lombok.extern.slf4j.Slf4j;


/**
 * Find field use relations (Uses and UsedBy) between a statement and the declaration of a field it uses.
 */

@Slf4j
public class CallsRelFinder extends VoidVisitorAdapter<List<DependencyRelation>>{

	public CallsRelFinder() {
		super();
	}
	
	private Optional<Node> tryResolveMethodDeclaration(MethodCallExpr n){
		
		try {
			ResolvedMethodDeclaration resolved=n.resolve();
			return resolved.toAst();
		} catch (UnsolvedSymbolException e) {
			return Optional.empty();
		}
	}
	
	@Override
	public void visit(MethodCallExpr n, List<DependencyRelation> rels) {
		super.visit(n, rels);
		
		Optional<Node> methodDeclaration=tryResolveMethodDeclaration(n);
		if(methodDeclaration.isPresent()) {
			
			DependencyRelation childToParent=DependencyRelation.builder()
					.label(DependencyLabel.CALLS)
					.origin(Util.toNodeId(n))
					.destiny(Util.toNodeId(methodDeclaration.get()))
					.build();
			
			DependencyRelation parentToChild=DependencyRelation.builder()
					.label(DependencyLabel.CALLED_BY)
					.origin(Util.toNodeId(methodDeclaration.get()))
					.destiny(Util.toNodeId(n))
					.build();

			rels.addAll(Arrays.asList(childToParent,parentToChild));
		}
		
	}

	
}