package jmdevall.opencodeplan.adapter.out.cusource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;
import jmdevall.opencodeplan.adapter.out.repository.CuSourceFactory;
import jmdevall.opencodeplan.adapter.out.repository.RepositoryMulpleFolders;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;

public class CuSourceFolterTest {

	TestingUtil testUtil=new TestingUtil();
	
	@Test
	public void canReadSourceCode() {
		File root=testUtil.getSrcTestFile("/testbench");
		//CuSourceFolder sut=CuSourceFolder.newDefaultJavaCuSourceFolder(testUtil.getSrcRootTestFolder());
		CuSource sut=RepositoryMulpleFolders.newFromSingleSourceRoot(root).getCuSource();
		
		String sourceCode=sut.getSource(sut.getPaths().iterator().next());
		assertNotNull(sourceCode);
	}
}
