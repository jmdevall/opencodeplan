package jmdevall.opencodeplan.domain.promptmaker;

import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.instruction.Instruction;
import jmdevall.opencodeplan.domain.plangraph.TemporalChange;
import jmdevall.opencodeplan.domain.plangraph.TemporalContext;

public class PromptMakerDefault implements PromptMaker {

	private InstructionTemplate instructionTemplate;
	
	public PromptMakerDefault(String intructionTemplateName) {
		this.instructionTemplate=InstructionTemplate.find(intructionTemplateName);
	}
	
	public PromptMakerDefault(InstructionTemplate instructionTemplate) {
		this.instructionTemplate=instructionTemplate;
	}
	
	private static final String  p1=
	 "Task Instruction: Your task is to ...\n%s";

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
	public String makePrompt(Fragment fragment, Instruction i, Context context){
		
		StringBuilder sb=new StringBuilder();
		sb.append(String.format(p1, i.getSpecificInstruction()));
		sb.append("\n\n");
		
		sb.append(p2);
		
		
		//TemporalContext
		TemporalContext tc=context.getTemporalContext();
		if(tc.getChanges().isEmpty()) {
			sb.append("No Earlier code changes before\n\n");
		}
		else {
			int numEdit=1;
			for(TemporalChange change:tc.getChanges()) {
				sb.append("Edit ");
				sb.append(numEdit);
				sb.append("\n");

				sb.append("Before:\n");
				sb.append(javaFragmentPrompt(change.getFragment().getOriginalcu().prompt()));
				sb.append("\n");
				
				sb.append("After:\n");
				sb.append(javaFragmentPrompt(change.getFragment().getRevised().prompt()));
				sb.append("\n");
				
				if(change.getCause()!=null){
					sb.append(p3);
					sb.append("«code_to_be_edited» is related to «code_changed_earlier» by «cause»="+change.getCause().getDependencyLabel());
				}
				
			}
		}
		
		
		/**
		sb.append("No Earlier code changes before\n\n");
		
		sb.append(p3);  //TODO:
		sb.append("No Earlier code changes before\n\n");
		
		**/
		
		sb.append(p4);
		for(String javacode:context.getSpatialContext()) {
			sb.append(javaFragmentPrompt(javacode));
		}
		
		sb.append(p5);
		sb.append(javaFragmentPrompt(fragment.getPrunedcu().prompt()));
		
		sb.append(finalInstruction);
		
		return String.format(instructionTemplate.getTemplate(), sb.toString());		
	}
	
	private String javaFragmentPrompt(String javacode) {
		StringBuilder sb=new StringBuilder();
		sb.append("```java\n");
		sb.append(javacode);
		sb.append("\n```\n\n");
		return sb.toString();
	}
}
