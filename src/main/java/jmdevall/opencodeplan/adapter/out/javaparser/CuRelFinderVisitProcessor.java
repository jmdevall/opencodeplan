package jmdevall.opencodeplan.adapter.out.javaparser;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import jmdevall.opencodeplan.domain.dependencygraph.Rel;
import lombok.Getter;

public class CuRelFinderVisitProcessor implements CuProcessor{

	private VoidVisitorAdapter<List<Rel>> visitor;
	
	@Getter
	private List<Rel> rels=new ArrayList<Rel>();
	
	public CuRelFinderVisitProcessor(VoidVisitorAdapter<List<Rel>> visitor) {
		super();
		this.visitor = visitor;
	}

	@Override
	public void process(CompilationUnit cu) {
		visitor.visit(cu, rels);
	}


}
