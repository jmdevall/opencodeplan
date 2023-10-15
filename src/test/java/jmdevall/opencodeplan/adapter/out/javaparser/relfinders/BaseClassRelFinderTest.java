package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuExplorer;
import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.Rel;

public class BaseClassRelFinderTest {

	private TestUtil testUtil=new TestUtil();

	@Test
	public void nose() {
		BaseClassRelFinder sut=new BaseClassRelFinder();
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		
		String startfolder=",testbench,testutil,implementation".replaceAll(",", File.separator);

		new CuExplorer(vp,
				(int level, String path, File file)->path.startsWith(startfolder))
				.explore(testUtil.getRootTestbenchFolder());
		
		List<Rel> rels = vp.getRels();
		logRels(rels);
		
		//TODO:
		assertEquals(1,rels.size());
	}

	private void logRels(List<Rel> rels) {
		for(Rel rel:rels) {
			System.out.println("rel="+rel);
		}
	}
}
