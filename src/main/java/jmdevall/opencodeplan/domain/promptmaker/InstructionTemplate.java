package jmdevall.opencodeplan.domain.promptmaker;

import lombok.Getter;

@Getter
public enum InstructionTemplate {
	
	ALPACA("alpaca",
	"Below is an instruction that describes a task. Write a response that appropriately completes the request.\n"
    +"\n"
	+"### Instruction:\n"
	+"%s\n"
    +"\n"
	+"### Response:\n"),
	
	CHATML("chatml",
			"<|im_start|>system\n"
			+"Below is an instruction that describes a task. Write a response that appropriately completes the request.\n"
			+ "<|im_end|>\n"
			+ "<|im_start|>user\n"
			+ "%s<|im_end|>\n"
			+ "<|im_start|>assistant\n");

	private String name;
	private String template;

	InstructionTemplate(String name, String template){
		this.name=name;
		this.template=template;
	}
	
	public static InstructionTemplate find(String name){
		for(InstructionTemplate i:InstructionTemplate.values()) {
			if(i.name.equals(name)) {
				return i;
			}
		}
		throw new IllegalArgumentException("Instruction Template not exists");
	}
}
