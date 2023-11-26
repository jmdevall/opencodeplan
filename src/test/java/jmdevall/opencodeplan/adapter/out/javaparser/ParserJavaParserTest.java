package jmdevall.opencodeplan.adapter.out.javaparser;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;

public class ParserJavaParserTest {

	@Test
	public void casoQueEstaFallando() {
		ParserJavaParser sut=new ParserJavaParser();
		
		TestingUtil testingUtil=new TestingUtil();
		String codigo=testingUtil.readFileFromTestSource("/caso2/caso.txt");
		
		sut.parse(codigo);
		
		
	}
}
