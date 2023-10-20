package jmdevall.opencodeplan.domain.instruction;

public class Inatural implements I {

    private String specificInstruction;

	public Inatural(String specificInstruction) {
		super();
		this.specificInstruction = specificInstruction;
	}

	@Override
	public String getSpecificInstruction() {
		return this.specificInstruction;
	}
}
