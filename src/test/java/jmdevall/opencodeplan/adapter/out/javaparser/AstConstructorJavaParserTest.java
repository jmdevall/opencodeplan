package jmdevall.opencodeplan.adapter.out.javaparser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.util.TestUtil;
import jmdevall.opencodeplan.domain.dependencygraph.Node;

public class AstConstructorJavaParserTest {

	private TestUtil testUtil=new TestUtil();
	
	@Test
	public void canFindCompilationUnits() {
		
		AstConstructorJavaParser sut=new AstConstructorJavaParser();
		new CuExplorer(sut).explore(testUtil.getRootTestbenchFolder());
		HashMap<String, Node> forest=sut.getForest();
		assertTrue(forest.containsKey("testbench.testutil.Multiclass"));
		
	}
	
	
	@Test
	public void foo() {
		
		AstConstructorJavaParser sut=new AstConstructorJavaParser();
		new CuExplorer(sut).explore(testUtil.getRootTestbenchFolder());
		HashMap<String, Node> forest=sut.getForest();
		
		ArrayList<String> ficheros=new ArrayList<String>(forest.keySet());
		Node cu=forest.get(ficheros.get(0));
		cu.debugRecursive(0);
		
	}
	

}
