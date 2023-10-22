package jmdevall.opencodeplan.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FragmentTest {
	@Test
	public void nose() {
		String texto = "hola\nmundo\n";
		String[] lineas = texto.split("\n");
		long numLineas = lineas.length;
		assertEquals(2,numLineas);
		
	}
	
	
	
	private String str1=
			"abcde casidro\n"+
	        "foo bar\n"+
			"ddd mm"		;
	
	private String str2=
			"manolo\n"+
	        "lala\n"+
			"mem";
	
	String machaca(String str1, String str2, int line, int col) {
	    String[] lines1 = str1.split("\n");
	    String[] lines2 = str2.split("\n");
	    StringBuilder resultado = new StringBuilder();
	    for (int i = 0; i < lines1.length; i++) {
	        if (i == line) {
	            String[] cols1 = lines1[i].split(" ");
	            String[] cols2 = lines2[i].split(" ");
	            StringBuilder lineResult = new StringBuilder();
	            for (int j = 0; j < cols1.length; j++) {
	                if (j == col) {
	                    lineResult.append(cols2[j]);
	                } else {
	                    lineResult.append(cols1[j]);
	                }
	                if (j < cols1.length - 1) {
	                    lineResult.append(" ");
	                }
	            }
	            resultado.append(lineResult.toString());
	        } else {
	            resultado.append(lines1[i]);
	        }
	        if (i < lines1.length - 1) {
	            resultado.append("\n");
	        }
	    }
	    return resultado.toString();
	}

	
	@Test
	public void testMachaca() {
		assertEquals(expected,machaca(str1,str2,0,7));
	}
	//resultado esperado de invocar con estos parametros:
	//	(str1, str2, 0, 7)....
		
	String expected="abcde manolo\n"+
	"lala\n"+
	"mem mm";
	      
			
}
