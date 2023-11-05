package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyLabel;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;
import lombok.extern.slf4j.Slf4j;


/**
 * Find field use relations (Uses and UsedBy) between a statement and the declaration of a field it uses.
 */

@Slf4j
public class UsesRelFinder extends VoidVisitorAdapter<List<DependencyRelation>>{

	public UsesRelFinder() {
		super();
	}
	
	@Override
	public void visit(NameExpr n, List<DependencyRelation> rels) {
		super.visit(n, rels);
		
		findRelsOfNameExprOrFieldAccessExpr(n, rels);
	}
	
	@Override
	public void visit(FieldAccessExpr n, List<DependencyRelation> rels) {
		super.visit(n, rels);
		
		findRelsOfNameExprOrFieldAccessExpr(n, rels);
	}

	private void findRelsOfNameExprOrFieldAccessExpr(Node n, List<DependencyRelation> rels) {
		Optional<Node> ofieldDeclaration = tryResolveFieldDeclaration(n);
		if(!ofieldDeclaration.isPresent()) {
			return;
		}
		Optional<Statement> ostmt=findStatementAncestor(n);
		if(!ostmt.isPresent()) {
			//TODO: should not ocurr: all access expression is inside a stmt
			return;
		}
		
		Node fieldDeclaration=ofieldDeclaration.get();
		Statement stmt=ostmt.get();
		
		rels.add(DependencyRelation.builder()
				.label(DependencyLabel.USES)
				.origin(Util.toNodeId(stmt))
				.destiny(Util.toNodeId(fieldDeclaration))
				.build());
		
		rels.add(DependencyRelation.builder()
				.label(DependencyLabel.USED_BY)
				.origin(Util.toNodeId(fieldDeclaration))
				.destiny(Util.toNodeId(stmt))
				.build());
	}
	
	private Optional<Node> tryResolveFieldDeclaration(Node n) {
		try {
			ResolvedValueDeclaration resolved = n.getSymbolResolver().resolveDeclaration(n, ResolvedValueDeclaration.class);
			
			if(resolved.isField()) {
				return resolved.toAst();
			}
			else {
				return Optional.empty();
			}
			
		} catch (UnsolvedSymbolException e) {
			return Optional.empty();
		}
	}

	private Optional<Statement> findStatementAncestor(Node n) {
		return n.findAncestor(Statement.class);
	}
	
}