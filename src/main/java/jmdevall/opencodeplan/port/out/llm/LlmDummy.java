package jmdevall.opencodeplan.port.out.llm;

import jmdevall.opencodeplan.adapter.out.llm.Llm;
import jmdevall.opencodeplan.domain.Fragment;

public class LlmDummy implements Llm {
	
    @Override
	public Fragment invoke(String prompt) {
        // TODO:
        return new Fragment();
    }
}