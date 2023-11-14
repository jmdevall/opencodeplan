package jmdevall.opencodeplan.adapter.out.javaparser.cusource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;

public class CuSourceFolterTest {

	TestingUtil testUtil=new TestingUtil();
	
	@Test
	public void canReadSourceCode() {
		File root=testUtil.getSrcTestFile("/testbench");
		//CuSourceFolder sut=CuSourceFolder.newDefaultJavaCuSourceFolder(testUtil.getSrcRootTestFolder());
		CuSource sut=CuSourceFactory.newDefaultJavaCuSourceFolder(root);
		
		String sourceCode=sut.getSource(sut.getPaths().iterator().next());
		assertNotNull(sourceCode);
	}
}
