package jmdevall.opencodeplan.domain.promptmaker;

import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.instruction.Instruction;

public interface PromptMaker {

	String makePrompt(Fragment fragment, Instruction i, Context context);

}