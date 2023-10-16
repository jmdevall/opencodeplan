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
/**
 * Field use relations
 */

@Slf4j
public class UsesRelFinder extends VoidVisitorAdapter<List<Rel>>{

	public UsesRelFinder() {
		super();
	}
	

	
	
	@Override
	public void visit(FieldDeclaration n, List<Rel> arg) {
		System.out.println("declaracion "+n);
		
		super.visit(n, arg);
		
	    String fieldName = n.getVariable(0).getNameAsString();
	    String className = n.getParentNode().get().getParentNode().get().toString();
	    
	    System.out.println("fn="+fieldName);
	    System.out.println("cn="+className);

	    CompilationUnit cu=n.findCompilationUnit().get();
	    
	    //CompilationUnit cu = JavaParser.parse(new File("path/to/your/Class.java"));
	    cu.findAll(MethodDeclaration.class).forEach(method -> {
	    	System.out.println("metodo "+method);
	        method.findAll( VariableDeclarator.class).forEach(variable -> {
	        	System.out.println("variable "+variable);
	        	
	            if (variable.getNameAsString().equals(fieldName)) {
	                System.out.println("El método " + method.getNameAsString() + " usa el campo " + fieldName);
	            }
	        });
	    });
	}

	
	
	
}