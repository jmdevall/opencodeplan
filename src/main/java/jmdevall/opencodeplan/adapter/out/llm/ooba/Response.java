package jmdevall.opencodeplan.adapter.out.llm.ooba;

import java.util.List;

import lombok.Data;

@Data
public class Response {
	
	private List<ResultText> results;
	
	//{"results": [{"text": "=?\nAnswer: 4"}]}

}
