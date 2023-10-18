package jmdevall.opencodeplan.adapter.out.javaparser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.BaseClassRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.CallsRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.ChildParentRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.InstantiateRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.OverridesRelFinder;
import jmdevall.opencodeplan.adapter.out.javaparser.relfinders.UsesRelFinder;
import jmdevall.opencodeplan.domain.Node;
import jmdevall.opencodeplan.domain.Rel;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.port.out.ConstructDependencyGraph;
import jmdevall.opencodeplan.port.out.repository.Repository;

public class ConstructDependencyGraphJavaparser implements ConstructDependencyGraph{

	private List<VoidVisitorAdapter<List<Rel>>> relfinders;
	
	private ConstructDependencyGraphJavaparser(List<VoidVisitorAdapter<List<Rel>>> relfinders) {
		super();
		this.relfinders = relfinders;
	}
	
	public static ConstructDependencyGraphJavaparser newDefault() {
		List<VoidVisitorAdapter<List<Rel>>> relfinders=new ArrayList<VoidVisitorAdapter<List<Rel>>>();
		
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
		
		AstConstructorJavaParser sut=new AstConstructorJavaParser();
		new CuExplorer(sut).explore(srcRoot);
		HashMap<String, Node> forest=sut.getForest();

		ArrayList<Rel> rels=new ArrayList<Rel>();
		for(VoidVisitorAdapter<List<Rel>> relfinder:relfinders) {
			rels.addAll(findRels(relfinder,srcRoot));
		}
		
		return new DependencyGraph(forest, rels);
	}
	
	private List<Rel> findRels(VoidVisitorAdapter<List<Rel>> relfinder,File srcRoot) {
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(relfinder);
		
		new CuExplorer(vp,
				(int level, String path, File file)-> file.getName().endsWith(".java"))
				.explore(srcRoot);
		
		return vp.getRels();
	}
	

}
