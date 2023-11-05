package jmdevall.opencodeplan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.application.Llm;

public class LlmTest {
	
	String posibleOutputOfLlm=
			"blabla\n"
			+ "more blabla\n"
			+ "\n"
			+ "```java\n"
			+ "package foo;\n"
			+ "  class Bar\n"
			+ "etc\n"
			+ "```\n"
			+ "more explain";
	
	String expected=
			"package foo;\n"
			+ "  class Bar\n"
			+ "etc\n";
	@Test
	public void nose() {
		Llm sut=new Llm();
		String actual=sut.extractOnlyCodeOfResponse(posibleOutputOfLlm);
		assertEquals(expected,actual);
	}
}
