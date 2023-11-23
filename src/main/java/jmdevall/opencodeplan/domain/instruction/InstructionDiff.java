package jmdevall.opencodeplan.domain.instruction;

public class InstructionDiff implements Instruction{
	
    private static String EXAMPLE_DIFF=""
    +"+ class Complex {\n"
    +"+   float real;\n"
    +"+    float imag;\n"
    +"+    dict<string, string> metadata;\n"
    +"+ }\n"
    +"− tuple<float, float> create_complex(float a, float b)\n"
    +"+ Complex create_complex(float a, float b, dict metadata)\n"
    ;

    private String diff;
    private String natural;

    String specificInstruction;

	@Override
	public String getSpecificInstruction() {
		// TODO Auto-generated method stub
		return null;
	}
}
