package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;


//TODO: no se en java  a que se refiere exactamente. Lo dejo por el momento
public class ImportsRelFinder extends VoidVisitorAdapter<List<DependencyRelation>>{
	
	private String file;
	
	private List<ImportDeclaration> importDeclarations;
	private MethodDeclaration currentMethod;
	
	public ImportsRelFinder(String file) {
		super();
		this.file = file;
		this.importDeclarations=new ArrayList<ImportDeclaration>();
	}

	
	
	@Override
	public void visit(ImportDeclaration n, List<DependencyRelation> arg) {
		importDeclarations.add(n);
		
		super.visit(n, arg);
	}

	

	@Override
	public void visit(FieldAccessExpr n, List<DependencyRelation> arg) {
		System.out.println("fieldAccessExpre "+file+" "+n);
		// Get the name of the field access expression
	    String fieldName = n.getName().asString();

	    // Check if the field name is in the list of imports
	    for (ImportDeclaration importDeclaration : importDeclarations) {
	        String importName = importDeclaration.getNameAsString();
	        if (importName.endsWith("." + fieldName)) {
	            System.out.println("Field access " + fieldName + " uses import " + importName);
	        }
	    }
	}



	@Override
	public void visit(MethodDeclaration n, List<DependencyRelation> arg) {
		currentMethod=n;
		
		super.visit(n, arg);
	}



	@Override
	public void visit(ClassOrInterfaceDeclaration n, List<DependencyRelation> arg) {
		//n.getex
		// TODO Auto-generated method stub
		super.visit(n, arg);
	}
	
	
	
}