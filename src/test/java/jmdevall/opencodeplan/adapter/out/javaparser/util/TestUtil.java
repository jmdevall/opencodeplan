package jmdevall.opencodeplan.adapter.out.javaparser.util;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;

import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceFolder;

public class TestUtil {
	
	public String readFileFromTestSource(String filepath) {
		File file=getSrcTestFile(filepath);
		InputStream is;
		try {
			is = new FileInputStream(file);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (FileNotFoundException e) {
			fail();
			return null;
		}
	}

	public File getSrcTestFile(String filepath) {
		return new File(this.getSrcRootTestFolder().getAbsolutePath()+filepath);
	}
	
	public File getSrcRootTestFolder() {
		return new File("src/test/java");
	}

	public CuSourceFolder testingCuSourceFolder(String startfolder) {
		CuSourceFolder cuSource=CuSourceFolder.newFromRootFolderAndFilter(getSrcRootTestFolder(),
				(int level, String path, File file)->path.startsWith(startfolder));
		return cuSource;
	}
	

}
