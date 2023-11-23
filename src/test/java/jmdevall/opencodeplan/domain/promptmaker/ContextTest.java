package jmdevall.opencodeplan.domain.promptmaker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.DependencyGraphConstructorJavaparser;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.dependencygraph.LineColRange;
import jmdevall.opencodeplan.domain.instruction.Inatural;
import jmdevall.opencodeplan.domain.plangraph.PlanGraph;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextTest {

	
	private static class FakeRepository implements Repository{

		File srcRoot;
		
		public FakeRepository(File srcRoot) {
			super();
			this.srcRoot = srcRoot;
		}

		@Override
		public File getSrcRoot() {
			return this.srcRoot;
		}

		@Override
		public void save(String filepath, String newFileContent) {
			// TODO Auto-generated method stub
		}

		@Override
		public CuSource getCuSource() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	private TestingUtil testUtil=new TestingUtil();

	
	@Test
	public void canGatherSpatialContext() {
		Repository r=testUtil.getTestingRepository("/testbench");

		DependencyGraphConstructorJavaparser jp=DependencyGraphConstructorJavaparser.newDefault();
		
		DependencyGraph d= jp.constructDependencyGraph(r);
		
		Node searchNode=Node.builder()
		.id(NodeId.builder()
		 .file("/testbench/testutil/uses/ExampleClass.java")
		 .range(LineColRange.newRange(11,23,11,23))
		 .build())
		.build();
		
		Context c=Context.gatherContext(searchNode, r, d, new PlanGraph());
		log.debug("spatialContext="+c.getSpatialContext());
		assertEquals(6,c.getSpatialContext().size());
		
	}
	
	@Test
	public void makePrompt() {
		DependencyGraphConstructorJavaparser jp=DependencyGraphConstructorJavaparser.newDefault();
		
		String nemofinderRoot="..."; //un proyecto existente TODO: configuracion!
		File srcRoot = new File(nemofinderRoot+"/src/main/java");
		FakeRepository repository = new FakeRepository(srcRoot);
		DependencyGraph d= jp.constructDependencyGraph(repository);
		
		NodeId searchNode=NodeId.builder()
		 .file("/nemofinder/DictionarySpanish.java")
		 .range(LineColRange.newRange(17,5,17,5))
		 .build();

		
		Optional<Node> si=d.findFinalNodeContaining(searchNode);
		
		Context c=Context.gatherContext(si.get(), repository, d, new PlanGraph());
		
		Fragment f=Fragment.newFromPrunedCuNode(si.get().getRootParent(), searchNode);
		PromptMakerDefault pm=new PromptMakerDefault();
		Inatural i=new Inatural("Haz que el método devuelva un Collection");
		String prompt=pm.makePrompt(f, i, c);
		
		System.out.println("prompt="+prompt);
	}
	
	
}
