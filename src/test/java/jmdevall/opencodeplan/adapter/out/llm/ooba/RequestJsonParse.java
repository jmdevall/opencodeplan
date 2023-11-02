package jmdevall.opencodeplan.adapter.out.llm.ooba;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

public class RequestJsonParse {
	
	@Test
	public void canGenerateJson() {
		
		Request r=new Request();
		r.setPrompt("foo");
		r.setMax_new_tokens(200);
		r.setMax_tokens_seconds(0);
		r.setAuto_max_new_tokens(false);
		
		String expected="{\"prompt\":\"foo\",\"max_new_tokens\":200,\"auto_max_new_tokens\":false,\"max_tokens_seconds\":0}";
		
	    Gson gson = new Gson();
		String foo = gson.toJson(r);
		
		assertEquals(expected,foo);
	}
	
	@Test
	public void canParseJson() {
		String json="{   \"prompt\": \"2+2\",\n"
	    		+ "        \"max_new_tokens\": 250,\n"
	    		+ "        \"auto_max_new_tokens\": false,\n"
	    		+ "        \"max_tokens_second\": 0"
	    		+ "}";
	    Gson gson = new Gson();
	    
		Request parsed = gson.fromJson(json, Request.class);
		Request expected=new Request();
		expected.setPrompt("2+2");
		expected.setMax_new_tokens(250);
		expected.setAuto_max_new_tokens(false);
		expected.setMax_tokens_seconds(0);
		
		assertEqualsRequest(expected,parsed);
	
	}

	private void assertEqualsRequest(Request expected, Request actual) {
		assertEquals(expected.getPrompt(),actual.getPrompt());
		assertEquals(expected.getMax_new_tokens(),actual.getMax_new_tokens());
		assertEquals(expected.isAuto_max_new_tokens(),actual.isAuto_max_new_tokens());
		assertEquals(expected.getMax_tokens_seconds(),actual.getMax_tokens_seconds());
		
	}
}
