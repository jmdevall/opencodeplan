package jmdevall.opencodeplan.adapter.out.llm.ooba.jsonmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
	private String prompt;
	private int max_new_tokens;
	private boolean auto_max_new_tokens;
	private int max_tokens_seconds;
}
