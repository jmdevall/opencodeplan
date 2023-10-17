package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.ArrayList;


import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import javassist.compiler.ast.Variable;
import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.Label;
import jmdevall.opencodeplan.domain.Rel;
import lombok.extern.slf4j.Slf4j;
/**
 * Field use relations
 */

@Slf4j
public class UsesRelFinder extends VoidVisitorAdapter<List<Rel>>{

	public UsesRelFinder() {
		super();
	}
	

	
	
	//@Override
	public void visitrrr(FieldDeclaration field, List<Rel> arg) {
		//System.out.println("declaracion "+field);
		
		super.visit(field, arg);
		
	    String fieldName = field.getVariable(0).getNameAsString();
	    String className = field.getParentNode().get().getParentNode().get().toString();
	    
	    //System.out.println("fn="+fieldName);
	    //System.out.println("cn="+className);

	    //CompilationUnit cu=n.findCompilationUnit().get();
	    Optional<ClassOrInterfaceDeclaration> clase=field.findAncestor(ClassOrInterfaceDeclaration.class);
	    clase.ifPresent(
	    		c->{
	    			c.findAll(MethodDeclaration.class)
	    			.forEach(method -> findUse(field, method));
	    		});
	    		
	    
	    //CompilationUnit cu = JavaParser.parse(new File("path/to/your/Class.java"));
	    //cu.findAll(MethodDeclaration.class).forEach(method -> {
	    
	    //});
	}

	void findUse(FieldDeclaration f,MethodDeclaration method){
		
		String fieldName = f.getVariable(0).getNameAsString();
		 
		/*
		boolean methodDeclareTheSameName=false;
    	List<VariableDeclarationExpr> vars=method.findAll(VariableDeclarationExpr.class);
    	for(VariableDeclarationExpr vde:vars) {
    		NodeList<VariableDeclarator> variables=vde.getVariables();
    		for(VariableDeclarator vd:variables) {
    	   		log.debug("variable name="+vd.getName());
        	    if (vd.getName().toString().equals(fieldName)) {
        	    	methodDeclareTheSameName=true;
                	log.debug("method "+method.getNameAsString()+" declares the same name "+fieldName+" as variable as fieldName");
        	    }
    		}
      	}*/
		
		
    	/*
    	method.findAll( FieldAccessExpr.class).forEach(fa -> {
    		System.out.println("fieldAccessExpr="+fa.toString());
    		//fa.getSymbolResolver();
    		ResolvedValueDeclaration foo=fa.resolve();
    		Optional<Node> cosa=foo.toAst();
    		if(cosa.isPresent()) {
    			System.out.println("ast="+cosa.get());	
    		}
    		
    	});
    	*/
    	System.out.println("metodo "+method.getNameAsString());
    	
    	
    	
        method.findAll( NameExpr.class).forEach(name -> {
        	System.out.println("variable "+name);
        	
        	try {
				ResolvedValueDeclaration foo=name.resolve();
				Optional<Node> cosa=foo.toAst();
				if(cosa.isPresent()) {
					System.out.println("astname="+cosa.get());
					if(cosa.get().equals(f)) {
						System.out.println("eurika");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
        	
        	
        	
        	//Node parent=name.getParentNode().get();
        	//System.out.println("parent="+parent.getClass());
        	
            if (name.getNameAsString().equals(fieldName)) {
                System.out.println("El método " + method.getNameAsString() + " usa el campo " + fieldName);
            }
        });
        
	}
	
	/*
	NameExpr nameExpr = ...; // tu NameExpr aquí
	Node parentNode = nameExpr.getParentNode();

	if (parentNode instanceof FieldAccessExpr) {
	    FieldAccessExpr fieldAccessExpr = (FieldAccessExpr) parentNode;
	    String fieldName = fieldAccessExpr.getName();
	    String scope = fieldAccessExpr.getScope().toString();
	    System.out.println("El nombre del campo es: " + fieldName);
	    System.out.println("El alcance es: " + scope);
	} else {
	    System.out.println("El nodo padre no es un acceso a campo.");
	}
	*/
/*

	@Override
	public void visit(FieldAccessExpr n, List<Rel> arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
		System.out.println("fa="+n);
	}

/*
	@Override
	public void visit(NameExpr n, List<Rel> arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
		
		System.out.println("ne="+n);
		
		System.out.println(n.getBegin()+" "+n.getEnd());
		
		
		SymbolResolver solver=n.getSymbolResolver();
		solver.resolveDeclaration(n, null)
		//String foo=solver.toResolvedType(type, null);
		FieldDeclaration fieldDeclaration = solver.resolveDeclaration(n, FieldDeclaration.class);
		System.out.println("fieldDeclaration="+fieldDeclaration);
		/*
		ResolvedType resuelto = solver.toResolvedType(type, ResolvedType.class);
		log.debug("resolvedReferenceType "+resuelto.toString());
		// Obtén la declaración de tipo resuelta
		Optional<ResolvedReferenceTypeDeclaration> foo= resuelto.asReferenceType().getTypeDeclaration();
		ResolvedReferenceTypeDeclaration bar=foo.get();
		
		Optional<Node> basenodeop=bar.toAst();
		Node basenode = basenodeop.get();
		log.debug("interface="+Util.toNodeId(basenode));

	}
*/
	
	@Override
	public void visit(NameExpr n, List<Rel> arg) {
		

		
		
		try {
			
			
			SymbolResolver solver=n.getSymbolResolver();
			ResolvedValueDeclaration foo=solver.resolveDeclaration(n, ResolvedValueDeclaration.class);
			
			//ResolvedValueDeclaration foo=n.resolve();
			
			Optional<Node> cosa=foo.toAst();
			if(cosa.isPresent()) {
				System.out.println("name "+n.getNameAsString()+" "+n.getBegin());
				System.out.println("declaracion es="+cosa.get() +" "+cosa.get().getBegin());
				System.out.println("en el cu "+Util.getFileNameOfCompilationUnit(cosa.get().findCompilationUnit().get()));
				System.out.println("");
				/*if(cosa.get().equals(f)) {
					System.out.println("eurika");
				}*/
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		super.visit(n, arg);
	}

	
}