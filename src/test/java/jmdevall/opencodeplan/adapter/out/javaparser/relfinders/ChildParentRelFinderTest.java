package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceFolder;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.dependencygraph.Rel;

public class ChildParentRelFinderTest {

	private TestUtil testUtil=new TestUtil();

	@Test
	public void nose() {
		
		ChildParentRelFinder sut=new ChildParentRelFinder();
		
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		String startfolder=",testbench,testutil,childparent".replaceAll(",", File.separator);
		CuSourceFolder cuSource=CuSourceFolder.newFromRootFolderAndFilter(testUtil.getRootTestbenchFolder(),
				(int level, String path, File file)->path.startsWith(startfolder));

		CuSourceProcessor.process(cuSource, vp);

		
		
		List<Rel> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(4,rels.size());
	}


}
