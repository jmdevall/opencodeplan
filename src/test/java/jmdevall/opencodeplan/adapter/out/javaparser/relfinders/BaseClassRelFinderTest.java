package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceCreatorFolder;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;

public class BaseClassRelFinderTest {

	public TestUtil testUtil=new TestUtil();

	@Test
	public void findImplementations() {
		BaseClassRelFinder sut=new BaseClassRelFinder();
		
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		String startfolder=",testbench,testutil,implementation".replaceAll(",", File.separator);
		
		CuSourceCreatorFolder cuSource = testUtil.testingCuSourceFolder(startfolder);

		CuSourceProcessor.process(cuSource, vp);
		
		List<DependencyRelation> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(2,rels.size());
		assertEquals("Rel(origin=NodeId(file=/testbench/testutil/implementation/ExampleInterface.java, range=[3,1]->[5,1]), destiny=NodeId(file=/testbench/testutil/implementation/ExampleImpl.java, range=[3,1]->[8,1]), label=BASE_CLASS_OF)",rels.get(0).toString());
	}

	@Test
	public void findExtensions() {
		BaseClassRelFinder sut=new BaseClassRelFinder();
		
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		String startfolder=",testbench,testutil,extension".replaceAll(",", File.separator);		
		CuSourceCreatorFolder cuSource = testUtil.testingCuSourceFolder(startfolder);

		CuSourceProcessor.process(cuSource, vp);
		
		List<DependencyRelation> rels = vp.getRels();

		LogRelUtil.logRels(rels);
		
		assertEquals(2,rels.size());
		assertEquals(
				"Rel(origin=NodeId(file=/testbench/testutil/extension/BaseExample.java, range=[3,1]->[8,1]), destiny=NodeId(file=/testbench/testutil/extension/ExtensionExample.java, range=[3,1]->[12,1]), label=BASE_CLASS_OF)"
				,rels.get(0).toString());
	}
}
