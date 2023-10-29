package jmdevall.opencodeplan.domain.promptmaker;

import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.instruction.I;

public class PromptMakerDefault implements PromptMaker {

	private static final String  p1=
	 "TaskInstruction: Your task is to ...\n%s";
	
	/*
	
	String p2 = "Earlier Code Changes (Temporal Context): These are edits that have been made in the code-base previously";
			
	"Edit 1:"
	"Before: «code_before»"
	"After: «code_after»"
	
	String p3 = " Causes for Change: The change is required due to\n";
	
	
	*/
	
			
			
			
			
	@Override
	public String makePrompt(Fragment fragment, I i, Context context){
		
		StringBuilder sb=new StringBuilder();
		sb.append(String.format(p1, i.getSpecificInstruction()));
		return sb.toString();

		
	}
}
