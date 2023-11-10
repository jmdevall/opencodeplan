package jmdevall.opencodeplan.adapter.out.javaparser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import jmdevall.opencodeplan.adapter.out.javaparser.JavaParserFactory;
import jmdevall.opencodeplan.adapter.out.javaparser.Util;

public class UtilTest {

	TestUtil testUtil=new TestUtil();
	
	@Test
	public void canReadJavasFromTestbench() {
		String content = testUtil.readFileFromTestSource("/testbench/testutil/CanRead.java");
		String expected="package testbench.testutil;\n"
				+ "\n"
				+ "public class CanRead{}";
		assertEquals(expected,content);
	}

	@Test
	public void canAccessTestbenchRootFolder() {
		File testbenchRoot=testUtil.getSrcRootTestFolder();
		assertNotNull(testbenchRoot);
	}

	@Test
	public void getPublicClassOfCompilationUnit() {
		JavaParser javaparser=JavaParserFactory.newDefaultJavaParser(testUtil.getSrcRootTestFolder());
		
		ParseResult<CompilationUnit> cu=javaparser.parse(testUtil.readFileFromTestSource("/testbench/testutil/Multiclass.java"));
		assertTrue(cu.isSuccessful());
		
		assertEquals("Multiclass", Util.getPublicTypeOfCompilationUnit(cu.getResult().get()));
	}
	
	@Test
	public void getFileNameOfCompilationUnitWithPackage() {
		JavaParser javaparser=JavaParserFactory.newDefaultJavaParser(testUtil.getSrcRootTestFolder());
		
		String readFileFromTestSource = testUtil.readFileFromTestSource("/testbench/testutil/Multiclass.java");
		ParseResult<CompilationUnit> cu=javaparser.parse(readFileFromTestSource);
		String filename=Util.getFileNameOfCompilationUnit(cu.getResult().get());
		
		assertEquals("/testbench/testutil/Multiclass.java",filename);
	}

	@Test
	public void getFileNameOfCompilationUnitWithoutPackage() {
		JavaParser javaparser=JavaParserFactory.newDefaultJavaParser(testUtil.getSrcRootTestFolder());
		
		ParseResult<CompilationUnit> cu=javaparser.parse(testUtil.readFileFromTestSource("/CanRead.java"));
		String filename=Util.getFileNameOfCompilationUnit(cu.getResult().get());
		
		assertEquals("/CanRead.java",filename);
	}

	
}
