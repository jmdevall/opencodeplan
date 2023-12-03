package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyLabel;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;
import lombok.extern.slf4j.Slf4j;


/**
 * Find field use relations (Uses and UsedBy) between a statement and the declaration of a field it uses.
 */

@Slf4j
public class OverridesRelFinder extends VoidVisitorAdapter<List<DependencyRelation>>{

	public OverridesRelFinder() {
		super();
	}
	
	/*
	   public SymbolResolver getSymbolResolver() {
	        return findCompilationUnit().map(cu -> {
	            if (cu.containsData(SYMBOL_RESOLVER_KEY)) {
	                return cu.getData(SYMBOL_RESOLVER_KEY);
	            }
	            throw new IllegalStateException("Symbol resolution not configured: to configure consider setting a SymbolResolver in the ParserConfiguration");
	        }).orElseThrow(() -> new IllegalStateException("The node is not inserted in a CompilationUnit"));
	    }
*/
	
	
	private Optional<ResolvedMethodDeclaration> findMethod(ResolvedReferenceType type, String methodName,String descriptor){
		List<ResolvedMethodDeclaration> methods=type.getAllMethodsVisibleToInheritors();
		for(ResolvedMethodDeclaration method:methods) {
		
			try {
				System.out.println("qualified signature="+method.getQualifiedSignature());
				System.out.println("signature="+method.getSignature());
				
				
				System.out.println("methodName="+methodName);
				System.out.println("methodDescriptor="+descriptor);
				System.out.println("method.getName()"+method.getName());
				System.out.println("method.toDescriptor()"+method.toDescriptor());
				
				
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
	
	private Optional<ResolvedMethodDeclaration> findMethodByResolvedMethodDeclaration(ResolvedReferenceType type, ResolvedMethodDeclaration resolvedMethodImp){
		List<ResolvedMethodDeclaration> methods=type.getAllMethodsVisibleToInheritors();
		for(ResolvedMethodDeclaration method:methods) {
			if(method.getSignature().equals(resolvedMethodImp.getSignature())){
				return Optional.of(method); 
			}
			/*try {
				String signature2 = method.getSignature();
				
				
				if(method.getName().equals(methodName) && signature2.equals(signature)) {
					return Optional.of(method);
				}
			} catch (Exception e) {
				//TODO: en ciertos casos salta excepción no se por que
				return Optional.empty();
			}*/
		}
		return Optional.empty();
	}
	
	private Optional<Node> tryResolveMethodDeclaration(MethodDeclaration m){

		
		try {

			ResolvedMethodDeclaration resolved=m.resolve();
			log.debug("descriptor="+resolved.toDescriptor());
			
			List<ResolvedReferenceType> ancestors=resolved.declaringType().getAllAncestors();
			
			//resolved.declaringType().getA getAllAncestors();
			
			for(ResolvedReferenceType ancestor:ancestors) {
				log.debug("searching method "+m.toDescriptor()+" in ancestestor "+ ancestor.getQualifiedName());
				//Optional<ResolvedMethodDeclaration> metodoEnPadre=findMethod(ancestor,m.getName().toString(),m.toDescriptor());
				
				Optional<ResolvedMethodDeclaration> metodoEnPadre=findMethodByResolvedMethodDeclaration(ancestor,resolved);
				if(metodoEnPadre.isPresent()) {
					return metodoEnPadre.get().toAst();
				}
				
			}
			
			
		} catch (UnsolvedSymbolException e) {
			return Optional.empty();
		}
		return Optional.empty();
	}
	
	@Override
	public void visit(MethodDeclaration n, List<DependencyRelation> rels) {
		
		super.visit(n, rels);
		Optional<Node> methodDeclaration=tryResolveMethodDeclaration(n);
		if(methodDeclaration.isPresent()) {
			
			DependencyRelation childToParent=DependencyRelation.builder()
					.label(DependencyLabel.OVERRIDES)
					.origin(Util.toNodeId(n))
					.destiny(Util.toNodeId(methodDeclaration.get()))
					.build();
			
			DependencyRelation parentToChild=DependencyRelation.builder()
					.label(DependencyLabel.OVERRIDEN_BY)
					.origin(Util.toNodeId(methodDeclaration.get()))
					.destiny(Util.toNodeId(n))
					.build();

			rels.addAll(Arrays.asList(childToParent,parentToChild));
		}

	}
	
}