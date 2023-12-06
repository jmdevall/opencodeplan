package jmdevall.opencodeplan.application;

import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.domain.LlmResult;

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
	public LlmResult invoke(String prompt) {
		String result = llmEngine.generate(prompt);

		String codeExtracted = extractOnlyCodeOfResponse(result);
		if(!codeExtracted.isBlank()) {
			return LlmResult.newOk(codeExtracted);
		}
		if(resultContainsNoChangesString(result)) {
			return LlmResult.newNochange();
		}
		return LlmResult.newUnknown(result);
	}

	public boolean resultContainsNoChangesString(String result) {
		return result.toLowerCase().contains("no changes");
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