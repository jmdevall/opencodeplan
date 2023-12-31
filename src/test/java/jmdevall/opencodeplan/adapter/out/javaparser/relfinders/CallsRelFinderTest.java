package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.CuRelFinderVisitProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;
import jmdevall.opencodeplan.adapter.out.repository.RepositoryMulpleFolders;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;


public class CallsRelFinderTest {

	private TestingUtil testUtil=new TestingUtil();

	@Test
	public void findCalls() {
		CallsRelFinder sut=new CallsRelFinder();
		
		CuRelFinderVisitProcessor vp=new CuRelFinderVisitProcessor(sut);
		String startfolder=",testbench,testutil,calls".replaceAll(",", File.separator);
		CuSource cuSource=RepositoryMulpleFolders.newFromSingleSourceRoot(testUtil.getSrcRootTestFolder(),
				(int level, String path, File file)->path.startsWith(startfolder))
				.getCuSource();

		CuSourceProcessor.process(cuSource, vp, testUtil.getTestingJavaParser());

		
		
		List<DependencyRelation> rels = vp.getRels();
		LogRelUtil.logRels(rels);
		
		assertEquals(2,rels.size());
		assertEquals("Rel(origin=NodeId(file=/testbench/testutil/calls/ExampleClass.java, range=[7,3]->[7,7]), destiny=NodeId(file=/testbench/testutil/calls/ExampleClass.java, range=[10,2]->[12,2]), label=CALLS)",rels.get(0).toString());
	}


}
