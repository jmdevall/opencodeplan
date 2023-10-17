package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.Rel;
import lombok.extern.slf4j.Slf4j;


/**
 * Find field use relations (Uses and UsedBy) between a statement and the declaration of a field it uses.
 */

@Slf4j
public class UsesRelFinder extends VoidVisitorAdapter<List<Rel>>{

	public UsesRelFinder() {
		super();
	}
	
	//TODO: usar este método en lugar
	public Optional<ResolvedValueDeclaration> tryResolve(NameExpr n) {
	    try {
	        return Optional.of(n.resolve());
	    } catch (UnsolvedSymbolException e) {
	        return Optional.empty();
	    }
	}
	
	@Override
	public void visit(NameExpr n, List<Rel> arg) {
		super.visit(n, arg);

		//n.isSolved(); //no existe pero me lo sugiere phind
	
		ResolvedValueDeclaration resolved;
		try {
			resolved = n.resolve();
		} catch (Exception e) {
			return;
		}
		
		// el método continua....
		
		Optional<Node> declaracion=resolved.toAst();
		if(declaracion.isPresent()) {
			System.out.println("name "+n.getNameAsString()+" "+n.getBegin());
			System.out.println("name en compilationUnit"+Util.getFileNameOfCompilationUnit(Util.getCompilationUnit(n)));
			if(resolved.isField()) {
				System.out.println("es field");
			}
			System.out.println("declaracion="+declaracion.get() +" "+declaracion.get().getBegin());
			System.out.println("declaración en el cu "+Util.getFileNameOfCompilationUnit(declaracion.get().findCompilationUnit().get()));
			System.out.println("");
		}

	}

	@Override
	public void visit(FieldAccessExpr n, List<Rel> arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
		ResolvedValueDeclaration resolved=n.resolve();
		
		Optional<Node> declaracion=resolved.toAst();
		if(declaracion.isPresent()) {
			System.out.println("fae "+n.getNameAsString()+" "+n.getBegin());
			System.out.println("fae en compilationUnit"+Util.getFileNameOfCompilationUnit(Util.getCompilationUnit(n)));
			if(resolved.isField()) {
				System.out.println("es field");
			}
			System.out.println("declaracion="+declaracion.get() +" "+declaracion.get().getBegin());
			System.out.println("declaración en el cu "+Util.getFileNameOfCompilationUnit(declaracion.get().findCompilationUnit().get()));
			System.out.println("");
		}

	}

	
}