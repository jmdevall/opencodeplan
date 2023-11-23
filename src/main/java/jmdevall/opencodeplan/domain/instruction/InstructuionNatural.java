package jmdevall.opencodeplan.domain.instruction;

public class InstructuionNatural implements Instruction {

    private String specificInstruction;

	public InstructuionNatural(String specificInstruction) {
		super();
		this.specificInstruction = specificInstruction;
	}

	@Override
	public String getSpecificInstruction() {
		return this.specificInstruction;
	}
}
