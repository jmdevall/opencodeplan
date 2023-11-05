package jmdevall.opencodeplan.application;

import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.domain.Fragment;

public class Llm {

	private LlmEngine llmEngine;
	
	

	public Fragment invoke(String prompt) {
		String result = llmEngine.generate(prompt);

		String code=extractOnlyCodeOfResponse(result);

		//TODO
		return null;
	}

	public String extractOnlyCodeOfResponse(String input) {
		String[] lines = input.split("\n");
		
		boolean insideCodeBlock = false;
		StringBuffer sb = new StringBuffer();

		for (String line : lines) {
			if (line.startsWith("```")) {
				insideCodeBlock = !insideCodeBlock;
			} else if (insideCodeBlock) {
				sb.append(line);
				sb.append("\n");
			}
		}

		return sb.toString();
	}
}