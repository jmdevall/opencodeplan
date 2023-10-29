package jmdevall.opencodeplan.domain.promptmaker;

import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.instruction.I;

public interface PromptMaker {

	String makePrompt(Fragment fragment, I i, Context context);

}