package jmdevall.opencodeplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	public void extractOnlyCodeOfResponse() {
		Llm sut=new Llm(null);
		String actual=sut.extractOnlyCodeOfResponse(posibleOutputOfLlm);
		assertEquals(expected,actual);
	}
	
	String exampleRealOutput="The existing code is already updated according to the earlier code changes. Therefore, no changes are required in the \"Code to be Changed Next\".";

	@Test
	public void whatifNoCode() {
		Llm sut=new Llm(null);
		boolean nochanges=sut.resultContainsNoChangesString(exampleRealOutput);
		assertTrue(nochanges);
	}
	
}
