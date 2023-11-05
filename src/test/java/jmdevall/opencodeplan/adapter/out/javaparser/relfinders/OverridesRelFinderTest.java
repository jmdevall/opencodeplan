package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceFolder;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;


public class OverridesRelFinderTest {

	private TestUtil testUtil=new TestUtil();

	@Test
	public void findOverrides() {
		OverridesRelFinder sut=new OverridesRelFinder();
		
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		String startfolder=",testbench,testutil,overrides".replaceAll(",", File.separator);
		CuSourceFolder cuSource=CuSourceFolder.newFromRootFolderAndFilter(testUtil.getRootTestbenchFolder(),
				(int level, String path, File file)->path.startsWith(startfolder));

		CuSourceProcessor.process(cuSource, vp);
		
		
		
		
		List<DependencyRelation> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(4,rels.size());
		
		String expected="Rel(origin=NodeId(file=/testbench/testutil/overrides/C.java, range=[6,2]->[8,2]), destiny=NodeId(file=/testbench/testutil/overrides/B.java, range=[6,2]->[8,2]), label=OVERRIDES)";

		List<String> collect = rels.stream().map( DependencyRelation::toString ).collect(Collectors.toList());
		assertTrue(collect.contains(expected));
		

	}


}
