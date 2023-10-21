package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuExplorer;
import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.dependencygraph.Rel;


public class OverridesRelFinderTest {

	private TestUtil testUtil=new TestUtil();

	@Test
	public void findOverrides() {
		OverridesRelFinder sut=new OverridesRelFinder();
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		
		String startfolder=",testbench,testutil,overrides".replaceAll(",", File.separator);

		new CuExplorer(vp,
				(int level, String path, File file)->path.startsWith(startfolder))
				.explore(testUtil.getRootTestbenchFolder());
		
		List<Rel> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(4,rels.size());
		assertEquals("Rel(origin=NodeId(file=testbench/testutil/overrides/C.java, range=[6,2]->[8,2]), destiny=NodeId(file=testbench/testutil/overrides/B.java, range=[6,2]->[8,2]), label=OVERRIDES)",rels.get(0).toString());
	}


}
