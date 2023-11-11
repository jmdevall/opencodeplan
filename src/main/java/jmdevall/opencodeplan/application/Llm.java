package jmdevall.opencodeplan.application;

import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.domain.Fragment;

public class Llm {

	private LlmEngine llmEngine;

	public Llm(LlmEngine llmEngine) {
		super();
		this.llmEngine = llmEngine;
	}

	/**
	 * returns newFragment
	 * @param prompt
	 * @return
	 */
	public String invoke(String prompt) {
		String result = llmEngine.generate(prompt);

		return extractOnlyCodeOfResponse(result);
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