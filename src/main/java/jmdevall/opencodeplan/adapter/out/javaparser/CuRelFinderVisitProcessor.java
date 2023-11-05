package jmdevall.opencodeplan.adapter.out.javaparser;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;
import lombok.Getter;

public class CuRelFinderVisitProcessor implements CuProcessor{

	private VoidVisitorAdapter<List<DependencyRelation>> visitor;
	
	@Getter
	private List<DependencyRelation> rels=new ArrayList<DependencyRelation>();
	
	public CuRelFinderVisitProcessor(VoidVisitorAdapter<List<DependencyRelation>> visitor) {
		super();
		this.visitor = visitor;
	}

	@Override
	public void process(CompilationUnit cu) {
		visitor.visit(cu, rels);
	}


}
