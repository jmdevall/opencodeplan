package jmdevall.opencodeplan.domain;

public class I {

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


    private I(){
    }

    public static I fromDiff(String diff){
        I i=new I();
        i.diff=diff;
        return i;
    }

    public static I fromExample(){
        return fromDiff(EXAMPLE_DIFF);
    }

    public static I fromNaturalLanguageInstruction(String natural){
        I i=new I();
        i.natural=natural;     
        return i;
    }

    public String getDiff(){
        return this.diff;
    }

    public String getNatural(){
        return this.natural;
    }
    


}
