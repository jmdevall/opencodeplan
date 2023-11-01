package jmdevall.opencodeplan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ReplaceSpecTest {

	@Test
	public void nose() {
		List<ReplaceSpec> specs=Arrays.asList(
				   new ReplaceSpec(0,1,"man"), //reemplaza la primera letra 'c' por la cadena "man"
				   new ReplaceSpec(3,5,"limon") //reemplaza desde la posición 3 a 5, la parte de la cadena "io" y lo sustituye por "limon"
	    );
		
		specs.sort((o1, o2) -> o2.begin - o1.begin);
		
		assertEquals(("manamlimonn"),aplicar(specs, "camion"));
	}
	
	public String aplicar(List<ReplaceSpec> specs, String base) {
		  String baseCopy = base;
		  
		  for (ReplaceSpec spec : specs) {
		      String substring = baseCopy.substring(spec.begin, spec.end);
		      baseCopy = baseCopy.replace(substring, spec.toReplace);
		  }
		  return baseCopy;
	}

}
