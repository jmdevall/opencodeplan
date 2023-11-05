package jmdevall.opencodeplan.application.port.out.llm;

public interface LlmEngine {

	String generate(String prompt) throws LlmException;

}