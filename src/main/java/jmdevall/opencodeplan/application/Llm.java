package jmdevall.opencodeplan.application;

import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.domain.Fragment;

public class Llm {
	
	private LlmEngine llmEngine;

	public Fragment invoke(String prompt) {
		String result=llmEngine.generate(prompt);
		
		String nose(result);
	}
	
		
	
	public void nose(String input) {
	      String[] lines = input.split("\n");
	        boolean insideCodeBlock = false;

	        for (String line : lines) {
	            if (line.startsWith("```")) {
	                insideCodeBlock = !insideCodeBlock;
	            } else if (insideCodeBlock) {
	                System.out.println(line);
	            }
	        }
	}
}