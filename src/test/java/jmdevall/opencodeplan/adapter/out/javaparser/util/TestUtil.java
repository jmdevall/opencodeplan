package jmdevall.opencodeplan.adapter.out.javaparser.util;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;

public class TestUtil {
	public String readFileFromTestbench(String file) {
		InputStream is=this.getClass().getResourceAsStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return reader.lines().collect(Collectors.joining(System.lineSeparator()));
	}
	
	public File getRootTestbenchFolder() {
		URL url=this.getClass().getResource("/testbench/CanRead.java");
		try {
		    File file = new File(url.toURI());
		    return file.getParentFile();
		    
		} catch (URISyntaxException e) {
		    fail();
		}
		return null;
	}

}
