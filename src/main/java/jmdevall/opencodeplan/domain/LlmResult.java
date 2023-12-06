package jmdevall.opencodeplan.domain;

import lombok.Getter;

@Getter
public class LlmResult {
	private boolean ok;
	private boolean nochanges;
	private String newcode;
	private String llmresult;
	
	private LlmResult(boolean ok, boolean nochanges, String newcode,String llmresult) {
		this.ok=ok;
		this.nochanges=nochanges;
		this.newcode=newcode;
		this.llmresult=llmresult;
	}
	
	public static LlmResult newOk(String newcode) {
		return new LlmResult(true,false,newcode,null);
	}
	
	public static LlmResult newNochange() {
		return new LlmResult(true,true,null,null);
	}
	
	public static LlmResult newUnknown(String llmresult) {
		return new LlmResult(false,false,null,llmresult);
	}
	
}
