package jmdevall.opencodeplan.domain;

import lombok.Getter;

@Getter
public class LlmResult {
	private boolean ok;
	private boolean nochanges;
	private boolean moreThanOneChunk;
	private String newcode;
	private String llmresult;
	
	
	private LlmResult(boolean ok, boolean nochanges, boolean moreThanOneChunk, String newcode,String llmresult) {
		this.ok=ok;
		this.nochanges=nochanges;
		this.moreThanOneChunk=moreThanOneChunk;
		this.newcode=newcode;
		this.llmresult=llmresult;
	}
	
	public static LlmResult newOk(String newcode) {
		return new LlmResult(true,false,false,newcode,null);
	}
	
	public static LlmResult newNochange() {
		return new LlmResult(true,true,false,null,null);
	}
	
	public static LlmResult newUnknown(String llmresult) {
		return new LlmResult(false,false,false, null,llmresult);
	}

	public static LlmResult newMultipleChunks(String llmresult) {
		return new LlmResult(false,false,true, null,llmresult);
	}
	
}
