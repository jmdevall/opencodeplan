package jmdevall.opencodeplan.domain.promptmaker;

import java.util.Arrays;
import java.util.List;

public class DiffUtil {

	public static List<String> tolines(String fileContent){
		return Arrays.asList(fileContent.split(System.lineSeparator()));
	}

}
