package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuExplorer;
import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.dependencygraph.Rel;


public class CallsRelFinderTest {

	private TestUtil testUtil=new TestUtil();

	@Test
	public void findCalls() {
		CallsRelFinder sut=new CallsRelFinder();
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		
		String startfolder=",testbench,testutil,calls".replaceAll(",", File.separator);

		new CuExplorer(vp,
				(int level, String path, File file)->path.startsWith(startfolder))
				.explore(testUtil.getRootTestbenchFolder());
		
		List<Rel> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(2,rels.size());
		assertEquals("Rel(origin=NodeId(file=testbench/testutil/calls/ExampleClass.java, range=[7,3]->[7,7]), destiny=NodeId(file=testbench/testutil/calls/ExampleClass.java, range=[10,2]->[12,2]), label=CALLS)",rels.get(0).toString());
	}


}
