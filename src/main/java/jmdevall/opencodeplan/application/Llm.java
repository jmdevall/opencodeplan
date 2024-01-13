package jmdevall.opencodeplan.application;

import java.util.ArrayList;
import java.util.List;

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

		List<String> chunks = extractOnlyCodeOfResponse(result);
		if(chunks.isEmpty()) {
			if(resultContainsNoChangesString(result)) {
				return LlmResult.newNochange();
			}
		}
		if(chunks.size()==1) {
			return LlmResult.newOk(chunks.get(0));
		}
		else if(chunks.size()>1) {
			return LlmResult.newMultipleChunks(result);
		}

		return LlmResult.newUnknown(result);
	}

	public boolean resultContainsNoChangesString(String result) {
		return result.toLowerCase().contains("no changes");
	}

	//return a list of chunks of code from de response of llm
	public List<String> extractOnlyCodeOfResponse(String input) {
		List<String> chunks=new ArrayList<String>();
		
		String[] lines = input.split("\n");
		
		boolean insideCodeBlock = false;
		StringBuffer sb = new StringBuffer();

		int numcodeblock=0;
		for (String line : lines) {
			if (line.startsWith("```")) {
				insideCodeBlock = !insideCodeBlock;
				if(insideCodeBlock) {
					sb=new StringBuffer();
				}
				else {
					chunks.add(sb.toString());
				}
			} else if (insideCodeBlock) {
				sb.append(line);
				sb.append("\n");
			}
		}

		return chunks;
	}
	
	
}