package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.dependencygraph.Label;
import jmdevall.opencodeplan.domain.dependencygraph.Rel;
import lombok.extern.slf4j.Slf4j;


/**
 * Find field use relations (Uses and UsedBy) between a statement and the declaration of a field it uses.
 */

@Slf4j
public class OverridesRelFinder extends VoidVisitorAdapter<List<Rel>>{

	public OverridesRelFinder() {
		super();
	}
	
	
	private Optional<ResolvedMethodDeclaration> findMethod(ResolvedReferenceType type, String methodName,String descriptor){
		List<ResolvedMethodDeclaration> methods=type.getAllMethodsVisibleToInheritors();
		for(ResolvedMethodDeclaration method:methods) {
		
			try {
				if(method.getName().equals(methodName) && method.toDescriptor().equals(descriptor)) {
					return Optional.of(method);
				}
			} catch (Exception e) {
				//TODO: en ciertos casos salta excepción no se por que
				return Optional.empty();
			}
		}
		return Optional.empty();
	}
	
	private Optional<Node> tryResolveMethodDeclaration(MethodDeclaration m){

		
		try {

			ResolvedMethodDeclaration resolved=m.resolve();
			log.debug("descriptor="+resolved.toDescriptor());
			
			List<ResolvedReferenceType> ancestors=resolved.declaringType(). getAllAncestors();
			
			for(ResolvedReferenceType a:ancestors) {
				Optional<ResolvedMethodDeclaration> metodoEnPadre=findMethod(a,m.getName().toString(),m.toDescriptor());
				if(metodoEnPadre.isPresent()) {
					return metodoEnPadre.get().toAst();
				}
				log.debug("a "+a.getId()+ " "+ a.getQualifiedName());
			}
			
			
		} catch (UnsolvedSymbolException e) {
			return Optional.empty();
		}
		return Optional.empty();
	}
	
	@Override
	public void visit(MethodDeclaration n, List<Rel> rels) {
		
		super.visit(n, rels);
		Optional<Node> methodDeclaration=tryResolveMethodDeclaration(n);
		if(methodDeclaration.isPresent()) {
			
			Rel childToParent=Rel.builder()
					.label(Label.OVERRIDES)
					.origin(Util.toNodeId(n))
					.destiny(Util.toNodeId(methodDeclaration.get()))
					.build();
			
			Rel parentToChild=Rel.builder()
					.label(Label.OVERRIDEN_BY)
					.origin(Util.toNodeId(methodDeclaration.get()))
					.destiny(Util.toNodeId(n))
					.build();

			rels.addAll(Arrays.asList(childToParent,parentToChild));
		}

	}
	
}