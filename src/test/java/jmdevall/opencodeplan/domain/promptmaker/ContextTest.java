package jmdevall.opencodeplan.domain.promptmaker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.ConstructDependencyGraphJavaparser;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.dependencygraph.LineColRange;
import jmdevall.opencodeplan.domain.instruction.Inatural;

public class ContextTest {
	private TestUtil testUtil=new TestUtil();

	
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
		public Fragment extractCodeFragment(BI bi) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void save(String filepath, String newFileContent) {
			// TODO Auto-generated method stub
		}
		
	}
	
	@Test
	public void canGatherSpatialContext() {
		ConstructDependencyGraphJavaparser jp=ConstructDependencyGraphJavaparser.newDefault();
		
		FakeRepository repository = new FakeRepository(testUtil.getSrcRootTestFolder());
		DependencyGraph d= jp.construct(repository);
		
		Node searchNode=Node.builder()
		.id(NodeId.builder()
		 .file("/testbench/testutil/uses/ExampleClass.java")
		 .range(LineColRange.newRange(11,23,11,23))
		 .build())
		.build();
		
		Context c=Context.gatherContext(searchNode, repository, d);
		assertEquals(5,c.getSpatialContext().size());
		
	}
	
	@Test
	public void makePrompt() {
		ConstructDependencyGraphJavaparser jp=ConstructDependencyGraphJavaparser.newDefault();
		
		String nemofinderRoot="..."; //un proyecto existente TODO: configuracion!
		File srcRoot = new File(nemofinderRoot+"/src/main/java");
		FakeRepository repository = new FakeRepository(srcRoot);
		DependencyGraph d= jp.construct(repository);
		
		NodeId searchNode=NodeId.builder()
		 .file("/nemofinder/DictionarySpanish.java")
		 .range(LineColRange.newRange(17,5,17,5))
		 .build();

		
		Optional<Node> si=d.findFinalNodeContaining(searchNode);
		
		Context c=Context.gatherContext(si.get(), repository, d);
		
		Fragment f=Fragment.newFromPrunedCuNode(si.get().getRootParent(), searchNode);
		PromptMakerDefault pm=new PromptMakerDefault();
		Inatural i=new Inatural("Haz que el método devuelva un Collection");
		String prompt=pm.makePrompt(f, i, c);
		
		System.out.println("prompt="+prompt);
	}
	
	
}
