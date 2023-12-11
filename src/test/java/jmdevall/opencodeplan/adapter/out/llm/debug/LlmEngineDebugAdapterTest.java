package jmdevall.opencodeplan.adapter.out.llm.debug;

import java.io.File;
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
		File root=new File(System.getProperty("user.home"), ".opencodeplan/debug");
		root.mkdirs();
		 
		LlmEngineDebugAdapter sut=new LlmEngineDebugAdapter(new LlmEngineDummy(),root);
		
		sut.generate("prueba");
		
		sut.generate("otracosa");
	}
}
