package jmdevall.opencodeplan.adapter.out.llm.ooba;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LlmOobaIT {

	@Test
	public void seInvocaYObtieneRespuesta() throws Exception {
		
		LlmEngine sut=new LlmEngineOoba("http://localhost:5000/api");
		String response=sut.generate("2+2");
		
		log.debug("respuesta=["+response+"]");
		assertTrue(response.contains("4"));
		
	}
}
