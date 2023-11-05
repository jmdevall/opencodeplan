package jmdevall.opencodeplan.application.port.out.llm;

import jmdevall.opencodeplan.domain.Fragment;

public interface Llm {

	Fragment invoke(String prompt);

}