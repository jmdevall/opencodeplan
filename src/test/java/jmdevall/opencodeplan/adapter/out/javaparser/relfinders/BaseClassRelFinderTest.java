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
	public void findImplementations() {
		BaseClassRelFinder sut=new BaseClassRelFinder();
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		
		String startfolder=",testbench,testutil,implementation".replaceAll(",", File.separator);

		new CuExplorer(vp,
				(int level, String path, File file)->path.startsWith(startfolder))
				.explore(testUtil.getRootTestbenchFolder());
		
		List<Rel> rels = vp.getRels();
		logRels(rels);
		
		assertEquals(2,rels.size());
		assertEquals("Rel(origin=NodeId(file=testbench.testutil.implementation.ExampleInterface, begin=Position(line=3, column=1), end=Position(line=5, column=1)), destiny=NodeId(file=testbench.testutil.implementation.ExampleImpl, begin=Position(line=3, column=1), end=Position(line=8, column=1)), label=BASE_CLASS_OF)",rels.get(0).toString());
	}
	
	@Test
	public void findExtensions() {
		BaseClassRelFinder sut=new BaseClassRelFinder();
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		
		String startfolder=",testbench,testutil,extension".replaceAll(",", File.separator);

		new CuExplorer(vp,
				(int level, String path, File file)->path.startsWith(startfolder))
				.explore(testUtil.getRootTestbenchFolder());
		
		List<Rel> rels = vp.getRels();
		logRels(rels);
		
		assertEquals(2,rels.size());
		assertEquals(
				"Rel(origin=NodeId(file=testbench.testutil.extension.BaseExample, begin=Position(line=3, column=1), end=Position(line=7, column=1)), destiny=NodeId(file=testbench.testutil.extension.ExtensionExample, begin=Position(line=3, column=1), end=Position(line=12, column=1)), label=BASE_CLASS_OF)"
				,rels.get(0).toString());
	}
	

	private void logRels(List<Rel> rels) {
		for(Rel rel:rels) {
			System.out.println("rel="+rel);
		}
	}
}
