package jmdevall.opencodeplan.adapter.out.llm.openai;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.llm.ooba.LlmEngineOoba;
import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LlmOpenaiIT {

	@Test
	public void seInvocaYObtieneRespuesta() throws Exception {
		
		String model = "TheBloke_Mistral-7B-codealpaca-lora-GPTQ";
		String openApiKey = "foo";
		LlmEngine sut=new LlmEngineOpenai("http://localhost:5000",model,openApiKey);
		String response=sut.generate("2+2");
		
		log.debug("respuesta=["+response+"]");
		assertTrue(response.contains("4"));
		
	}
}
