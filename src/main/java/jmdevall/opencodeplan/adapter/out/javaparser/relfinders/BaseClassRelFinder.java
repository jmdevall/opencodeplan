package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.Label;
import jmdevall.opencodeplan.domain.Rel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseClassRelFinder extends VoidVisitorAdapter<List<Rel>>{

	public BaseClassRelFinder() {
		super();
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, List<Rel> rels) {
		NodeList<ClassOrInterfaceType> types;
		
		types=n.getExtendedTypes();
		for(ClassOrInterfaceType type:types) {
			System.out.println("La clase "+n.getName()+" extiende "+type.getName());
		}

		types=n.getImplementedTypes();
		for(ClassOrInterfaceType type:types) {
			log.debug("La clase "+n.getName()+" implementa "+type.getName());
			//Util.getTypeDeclaration(type);
			SymbolResolver solver=n.getSymbolResolver();
			//String foo=solver.toResolvedType(type, null);
			ResolvedType resuelto = solver.toResolvedType(type, ResolvedType.class);
			log.debug("resolvedReferenceType "+resuelto.toString());
			// Obtén la declaración de tipo resuelta
			Optional<ResolvedReferenceTypeDeclaration> foo= resuelto.asReferenceType().getTypeDeclaration();
			ResolvedReferenceTypeDeclaration bar=foo.get();
			
			Optional<Node> basenodeop=bar.toAst();
			Node basenode = basenodeop.get();
			log.debug("interface="+Util.toNodeId(basenode));
			
			rels.add(Rel.builder()
					.label(Label.BASE_CLASS_OF)
					.origin(Util.toNodeId(n))
					.destiny(Util.toNodeId(n))
					.build());

		}
		super.visit(n, rels);
	}
	
	
	
}