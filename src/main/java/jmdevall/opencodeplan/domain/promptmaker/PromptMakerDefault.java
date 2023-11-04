package jmdevall.opencodeplan.domain.promptmaker;

import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.instruction.I;

public class PromptMakerDefault implements PromptMaker {

	private static final String alpacaInstruction=
	"Below is an instruction that describes a task. Write a response that appropriately completes the request.\n"
    +"\n"
	+"### Instruction:\n"
	+"%s\n"
    +"\n"
	+"### Response:\n";
	
	private static final String  p1=
	 "TaskInstruction: Your task is to ...\n%s";

	private static final String p2 = "Earlier Code Changes (Temporal Context): These are edits that have been made in the code-base previously\n";

	private static final String p3 = "Causes for Change: The change is required due to\n";
	
	private static final String p4="Related Code (Spatial Context): The following code maybe related\n\n";
	
	private static final String p5="Code to be Changed Next: The existing code is given below -\n";
			
	private static final String finalInstruction=
	"Edit the \"Code to be Changed Next\" and produce \"Changed Code\" below. Edit the \"Code "
	+"to be Changed Next\" according to the \"Task Instructions\" to make it consistent with "
	+"the \"Earlier Code Changes\", \"Causes for Change\" and \"Related Code\". If no changes are "
	+"needed, output \"No changes.\"";
		
	@Override
	public String makePrompt(Fragment fragment, I i, Context context){
		
		StringBuilder sb=new StringBuilder();
		sb.append(String.format(p1, i.getSpecificInstruction()));
		sb.append("\n\n");
		
		sb.append(p2);  //TODO:
		sb.append("No Earlier code changes before\n\n");
		
		sb.append(p3);  //TODO:
		sb.append("No Earlier code changes before\n\n");
		
		sb.append(p4);
		for(String javacode:context.getSpatialContext()) {
			sb.append(javaFragmentPrompt(javacode));
		}
		
		sb.append(p5);
		sb.append(javaFragmentPrompt(fragment.getNode().prompt()));
		
		sb.append(finalInstruction);
		
		return String.format(alpacaInstruction, sb.toString());		
	}
	
	private String javaFragmentPrompt(String javacode) {
		StringBuilder sb=new StringBuilder();
		sb.append("```java\n");
		sb.append(javacode);
		sb.append("\n```\n\n");
		return sb.toString();
	}
}
