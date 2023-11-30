package jmdevall.opencodeplan.adapter.out.llm.debug;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.application.port.out.llm.LlmException;

public class LlmEngineDebugAdapterTest {

	private static class LlmEngineDummy implements LlmEngine{

		@Override
		public String generate(String prompt) throws LlmException {
			return "hola "+prompt;
		}
		
	}
	
	@Test
	public void createTempFolder() throws IOException {
		LlmEngineDebugAdapter sut=new LlmEngineDebugAdapter(new LlmEngineDummy());
		
		sut.generate("prueba");
		
		sut.generate("otracosa");
	}
}
