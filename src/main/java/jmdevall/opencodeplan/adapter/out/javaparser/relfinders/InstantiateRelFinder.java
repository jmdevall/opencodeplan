package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.Label;
import jmdevall.opencodeplan.domain.Rel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstantiateRelFinder extends VoidVisitorAdapter<List<Rel>>{

	public InstantiateRelFinder() {
		super();
	}

	private Optional<Node> tryResolveConstructionDeclaration(ObjectCreationExpr n){
		
		try {
			ResolvedConstructorDeclaration resolved=n.resolve();
			return resolved.toAst();
		} catch (UnsolvedSymbolException e) {
			return Optional.empty();
		}
	}
	
	@Override
	public void visit(ObjectCreationExpr n, List<Rel> rels) {
		super.visit(n, rels);
		Optional<Node> constructionDeclaration=tryResolveConstructionDeclaration(n);
		
		if(constructionDeclaration.isPresent()) {
			
			Rel childToParent=Rel.builder()
					.label(Label.CONSTRUCTS)
					.origin(Util.toNodeId(n))
					.destiny(Util.toNodeId(constructionDeclaration.get()))
					.build();
			
			Rel parentToChild=Rel.builder()
					.label(Label.CONSTRUCTED_BY)
					.origin(Util.toNodeId(constructionDeclaration.get()))
					.destiny(Util.toNodeId(n))
					.build();

			rels.addAll(Arrays.asList(childToParent,parentToChild));
		}

	}

	
	
}