package jmdevall.opencodeplan.adapter.out.javaparser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import jmdevall.opencodeplan.adapter.out.javaparser.Util;

public class UtilTest {

	TestUtil testUtil=new TestUtil();
	
	@Test
	public void canReadJavasFromTestbench() {
		String content = testUtil.readFileFromTestbench("/testbench/CanRead.java");
		
		assertEquals("public class CanRead{}",content);
	}

	@Test
	public void canAccessTestbenchRootFolder() {
		File testbenchRoot=testUtil.getRootTestbenchFolder();
		assertNotNull(testbenchRoot);
	}

	@Test
	public void getPublicClassOfCompilationUnit() {
		JavaParser javaparser=Util.newDefaultJavaParser(testUtil.getRootTestbenchFolder());
		
		ParseResult<CompilationUnit> cu=javaparser.parse(testUtil.readFileFromTestbench("/testbench/testbench/testutil/Multiclass.java"));
		assertTrue(cu.isSuccessful());
		
		assertEquals("Multiclass", Util.getPublicTypeOfCompilationUnit(cu.getResult().get()));
	}
	
	@Test
	public void getFileNameOfCompilationUnitWithPackage() {
		JavaParser javaparser=Util.newDefaultJavaParser(testUtil.getRootTestbenchFolder());
		
		ParseResult<CompilationUnit> cu=javaparser.parse(testUtil.readFileFromTestbench("/testbench/testbench/testutil/Multiclass.java"));
		String filename=Util.getFileNameOfCompilationUnit(cu.getResult().get());
		
		assertEquals("testbench.testutil.Multiclass",filename);
	}

	@Test
	public void getFileNameOfCompilationUnitWithoutPackage() {
		JavaParser javaparser=Util.newDefaultJavaParser(testUtil.getRootTestbenchFolder());
		
		ParseResult<CompilationUnit> cu=javaparser.parse(testUtil.readFileFromTestbench("/testbench/CanRead.java"));
		String filename=Util.getFileNameOfCompilationUnit(cu.getResult().get());
		
		assertEquals("CanRead",filename);
	}

	
}
