package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSource;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceFactory;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;

public class UsesRelFinderTest {

	private TestingUtil testUtil=new TestingUtil();

	@Test
	public void findUsesRels() {
		UsesRelFinder sut=new UsesRelFinder();
		
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		String startfolder=",testbench,testutil,uses".replaceAll(",", File.separator);
		CuSource cuSource=CuSourceFactory.newFromRootFolderAndFilter(testUtil.getSrcRootTestFolder(),
				(int level, String path, File file)->path.startsWith(startfolder));

		CuSourceProcessor.process(cuSource, vp, testUtil.getTestingJavaParser());
		
		
		List<DependencyRelation> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(22,rels.size());
	}

}
