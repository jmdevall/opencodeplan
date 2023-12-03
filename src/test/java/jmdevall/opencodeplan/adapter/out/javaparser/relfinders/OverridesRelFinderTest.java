package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;
import jmdevall.opencodeplan.adapter.out.repository.RepositoryFile;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;


public class OverridesRelFinderTest {

	private TestingUtil testUtil=new TestingUtil();

	@Test
	public void findOverrides() {
		OverridesRelFinder sut=new OverridesRelFinder();
		
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		String startfolder=",testbench,testutil,overrides".replaceAll(",", File.separator);
		CuSource cuSource=RepositoryFile.newRepositoryFile(testUtil.getSrcRootTestFolder(),
				(int level, String path, File file)->path.startsWith(startfolder)).getCuSource();

		CuSourceProcessor.process(cuSource, vp, testUtil.getTestingJavaParser());
		
		
		
		
		List<DependencyRelation> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(4,rels.size());
		
		String expected="Rel(origin=NodeId(file=/testbench/testutil/overrides/C.java, range=[6,2]->[8,2]), destiny=NodeId(file=/testbench/testutil/overrides/B.java, range=[6,2]->[8,2]), label=OVERRIDES)";

		List<String> collect = rels.stream().map( DependencyRelation::toString ).collect(Collectors.toList());
		assertTrue(collect.contains(expected));
		

	}
	
	@Test
	public void findOverridesInterfaces() {
		OverridesRelFinder sut=new OverridesRelFinder();
		
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		String startfolder=",testbench,testutil,implementation".replaceAll(",", File.separator);
		CuSource cuSource=RepositoryFile.newRepositoryFile(testUtil.getSrcRootTestFolder(),
				(int level, String path, File file)->path.startsWith(startfolder)).getCuSource();

		CuSourceProcessor.process(cuSource, vp, testUtil.getTestingJavaParser());
		
		
		
		
		List<DependencyRelation> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(2,rels.size());
		
		String expected="Rel(origin=NodeId(file=/testbench/testutil/implementation/ExampleImpl.java, range=[5,2]->[7,2]), destiny=NodeId(file=/testbench/testutil/implementation/ExampleInterface.java, range=[4,2]->[4,33]), label=OVERRIDES)";

		List<String> collect = rels.stream().map( DependencyRelation::toString ).collect(Collectors.toList());
		assertTrue(collect.contains(expected));
		
	}


}
