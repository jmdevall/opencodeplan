package jmdevall.opencodeplan.adapter.out.llm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LlmOobaIT {

	@Test
	public void nose() throws Exception {
		
		LlmOoba sut=new LlmOoba();
		String response=sut.generate("2+2");
		
		log.debug("respuesta="+response);
		assertTrue(response.contains("4"));
		
	}
}
