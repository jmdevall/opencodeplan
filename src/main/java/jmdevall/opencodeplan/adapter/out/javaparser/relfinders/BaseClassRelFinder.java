package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
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
		addRels(n, types, rels);
		
		types=n.getImplementedTypes();
		addRels(n, types, rels);
		super.visit(n, rels);
	}

	private void addRels(ClassOrInterfaceDeclaration classOrInterfaceDecNode, NodeList<ClassOrInterfaceType> types,
			List<Rel> rels) {
		for(ClassOrInterfaceType type:types) {
			log.debug("La clase "+classOrInterfaceDecNode.getName()+" implementa o extiende "+type.getName());
			//Util.getTypeDeclaration(type);
			SymbolResolver solver=classOrInterfaceDecNode.getSymbolResolver();
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
					.origin(Util.toNodeId(basenode))
					.destiny(Util.toNodeId(classOrInterfaceDecNode))
					.build());
			
			rels.add(Rel.builder()
					.label(Label.DERIVED_CLASS_OF)
					.origin(Util.toNodeId(classOrInterfaceDecNode))
					.destiny(Util.toNodeId(basenode))
					.build());

		}
	}
	
	
	
}