package jmdevall.opencodeplan.adapter.out.javaparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.BaseClassRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.CallsRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.ChildParentRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.InstantiateRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.OverridesRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.UsesRelFinder;
import jmdevall.opencodeplan.application.port.out.parser.DependencyGraphConstructor;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;
import jmdevall.opencodeplan.domain.dependencygraph.Node;

public class DependencyGraphConstructorJavaparser implements DependencyGraphConstructor{

	private List<VoidVisitorAdapter<List<DependencyRelation>>> relfinders;
	
	private DependencyGraphConstructorJavaparser(List<VoidVisitorAdapter<List<DependencyRelation>>> relfinders) {
		super();
		this.relfinders = relfinders;
	}
	
	public static DependencyGraphConstructorJavaparser newDefault() {
		List<VoidVisitorAdapter<List<DependencyRelation>>> relfinders=new ArrayList<VoidVisitorAdapter<List<DependencyRelation>>>();
		
		relfinders.add(new ChildParentRelFinder());
		relfinders.add(new BaseClassRelFinder());
		relfinders.add(new UsesRelFinder());
		relfinders.add(new OverridesRelFinder());
		relfinders.add(new InstantiateRelFinder());
		relfinders.add(new CallsRelFinder());
		
		return new DependencyGraphConstructorJavaparser(relfinders);
	}

	@Override
	public DependencyGraph constructDependencyGraph(Repository repository) {
		
		CuSource cuSource=repository.getCuSource();
		
		AstConstructorJavaParser astcreator=new AstConstructorJavaParser(cuSource);
		JavaParser parser=JavaParserFactory.newDefaultJavaParser(repository.getBuildPath());
		CuSourceProcessor.process(cuSource, astcreator,parser);
		HashMap<String, Node> forest=astcreator.getForest();

		ArrayList<DependencyRelation> rels=new ArrayList<DependencyRelation>();
		for(VoidVisitorAdapter<List<DependencyRelation>> relfinder:relfinders) {
			rels.addAll(findRels(relfinder,cuSource, parser));
		}
		
		return new DependencyGraph(forest, rels);
	}
	
	private List<DependencyRelation> findRels(
			VoidVisitorAdapter<List<DependencyRelation>> relfinder, CuSource cuSource, JavaParser parser) {
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(relfinder);
		CuSourceProcessor.process(cuSource, vp, parser );
		
		return vp.getRels();
	}
	

}
