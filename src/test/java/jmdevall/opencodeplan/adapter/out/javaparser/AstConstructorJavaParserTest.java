package jmdevall.opencodeplan.adapter.out.javaparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AstConstructorJavaParserTest {

	private TestUtil testUtil=new TestUtil();
	
	@Test
	public void canFindCompilationUnits() {
		
		CuSourceFolder cuSource=CuSourceFolder.newFromFile(testUtil.getRootTestbenchFolder());
		
		AstConstructorJavaParser sut=new AstConstructorJavaParser(cuSource);
		CuSourceProcessor.process(cuSource, sut);

		HashMap<String, Node> forest=sut.getForest();
		String expectedName = ".testbench.testutil.Multiclass".replace(".", File.separator)+".java";
		log.debug("expectedname="+expectedName);
		assertTrue(forest.containsKey(expectedName));
		
	}
	
	
	@Test
	public void elResultadoDeHacerParseSobreCuEsElPropioContenidoDelCu() {
		
		CuSourceFolder cuSource=CuSourceFolder.newFromFile(testUtil.getRootTestbenchFolder());
		
		AstConstructorJavaParser sut=new AstConstructorJavaParser(cuSource);
		CuSourceProcessor.process(cuSource, sut);
		
		HashMap<String, Node> forest=sut.getForest();
		
		ArrayList<String> ficheros=new ArrayList<String>(forest.keySet());
		Node cu=forest.get(ficheros.get(0));
		cu.debugRecursive(0);
		
		assertEquals(cu.getContent(),cu.prompt());
		
	}
	
	@Test
	public void testComportamientoStringBuffer() {
		StringBuffer sut=new StringBuffer("cabeza");
		sut.replace(2, 3, "rp");
		
		assertEquals(sut.toString(),"carpeza");
	}
	
	@Test
	public void testComportamientoStringBufferQuitando() {
		StringBuffer sut=new StringBuffer("cabeza");
		sut.replace(2, 4, "l");
		
		assertEquals("calza",sut.toString());
	}
	
	

}
