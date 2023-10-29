package jmdevall.opencodeplan.adapter.out.llm;

import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.port.out.llm.Llm;

public class LlmDummy implements Llm {
	
    @Override
	public Fragment invoke(String prompt) {
        // TODO:
        return null;
    }
}