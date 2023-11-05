package jmdevall.opencodeplan.adapter.out.llm;

import jmdevall.opencodeplan.application.port.out.llm.Llm;
import jmdevall.opencodeplan.domain.Fragment;

public class LlmDummy implements Llm {
	
    @Override
	public Fragment invoke(String prompt) {
        // TODO:
        return null;
    }
}