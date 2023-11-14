package jmdevall.opencodeplan.adapter.out.javaparser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSource;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceFactory;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.BaseClassRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.CallsRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.ChildParentRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.InstantiateRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.OverridesRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.UsesRelFinder;
import jmdevall.opencodeplan.application.port.out.parser.ConstructDependencyGraph;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;

public class ConstructDependencyGraphJavaparser implements ConstructDependencyGraph{

	private List<VoidVisitorAdapter<List<DependencyRelation>>> relfinders;
	
	private ConstructDependencyGraphJavaparser(List<VoidVisitorAdapter<List<DependencyRelation>>> relfinders) {
		super();
		this.relfinders = relfinders;
	}
	
	public static ConstructDependencyGraphJavaparser newDefault() {
		List<VoidVisitorAdapter<List<DependencyRelation>>> relfinders=new ArrayList<VoidVisitorAdapter<List<DependencyRelation>>>();
		
		relfinders.add(new ChildParentRelFinder());
		relfinders.add(new BaseClassRelFinder());
		relfinders.add(new UsesRelFinder());
		relfinders.add(new OverridesRelFinder());
		relfinders.add(new InstantiateRelFinder());
		relfinders.add(new CallsRelFinder());
		
		return new ConstructDependencyGraphJavaparser(relfinders);
	}

	@Override
	public DependencyGraph construct(Repository repository) {
		File srcRoot=repository.getSrcRoot();
		
		CuSource cuSource=CuSourceFactory.newDefaultJavaCuSourceFolder(srcRoot);
		
		AstConstructorJavaParser astcreator=new AstConstructorJavaParser(cuSource);
		JavaParser parser=JavaParserFactory.newDefaultJavaParser(srcRoot);
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
