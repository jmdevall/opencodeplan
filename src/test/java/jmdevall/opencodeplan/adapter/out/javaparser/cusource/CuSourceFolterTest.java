package jmdevall.opencodeplan.adapter.out.javaparser.cusource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;

public class CuSourceFolterTest {

	TestUtil testUtil=new TestUtil();
	
	@Test
	public void canReadSourceCode() {
		File root=testUtil.getSrcTestFile("/testbench");
		//CuSourceFolder sut=CuSourceFolder.newDefaultJavaCuSourceFolder(testUtil.getSrcRootTestFolder());
		CuSourceCreatorFolder sut=CuSourceCreatorFolder.newDefaultJavaCuSourceFolder(root);
		
		String sourceCode=sut.getSource(sut.getPaths().get(0));
		assertNotNull(sourceCode);
	}
}
