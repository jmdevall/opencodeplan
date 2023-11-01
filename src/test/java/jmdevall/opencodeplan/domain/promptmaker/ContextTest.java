package jmdevall.opencodeplan.domain.promptmaker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.ConstructDependencyGraphJavaparser;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.dependencygraph.Range;
import jmdevall.opencodeplan.port.out.repository.Repository;

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
		public Repository merge(Fragment newFragment, Node b) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
	
	@Test
	public void canGatherSpatialContext() {
		ConstructDependencyGraphJavaparser jp=ConstructDependencyGraphJavaparser.newDefault();
		
		FakeRepository repository = new FakeRepository(testUtil.getRootTestbenchFolder());
		DependencyGraph d= jp.construct(repository);
		
		Node searchNode=Node.builder()
		.id(NodeId.builder()
		 .file("/testbench/testutil/uses/ExampleClass.java")
		 .range(Range.newRange(11,23,11,23))
		 .build())
		.build();
		
		Context c=Context.gatherContext(searchNode, repository, d);
		assertEquals(5,c.getSpatialContext().size());
		
	}
}
