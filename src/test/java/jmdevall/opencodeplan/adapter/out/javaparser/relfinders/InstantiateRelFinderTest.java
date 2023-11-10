package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceFolder;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;


public class InstantiateRelFinderTest {

	private TestUtil testUtil=new TestUtil();

	@Test
	public void findInstantiates() {
		InstantiateRelFinder sut=new InstantiateRelFinder();
		
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		String startfolder=",testbench,testutil,instantiate".replaceAll(",", File.separator);
		CuSourceFolder cuSource=CuSourceFolder.newFromRootFolderAndFilter(testUtil.getSrcRootTestFolder(),
				(int level, String path, File file)->path.startsWith(startfolder));

		CuSourceProcessor.process(cuSource, vp);

	
		
		List<DependencyRelation> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(2,rels.size());
		assertEquals("Rel(origin=NodeId(file=/testbench/testutil/instantiate/ExampleClass.java, range=[11,3]->[11,20]), destiny=NodeId(file=/testbench/testutil/instantiate/ExampleClass.java, range=[5,2]->[8,2]), label=CONSTRUCTS)",rels.get(0).toString());
	}


}
