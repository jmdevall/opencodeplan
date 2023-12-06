package jmdevall.opencodeplan.adapter.out.javaparser.util;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;

import jmdevall.opencodeplan.adapter.out.javaparser.JavaParserFactory;
import jmdevall.opencodeplan.adapter.out.repository.FiltersFactory;
import jmdevall.opencodeplan.adapter.out.repository.RepositoryMulpleFolders;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.application.port.out.repository.SourceFolder;

public class TestingUtil {
	
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
		File srcTestRoot = new File("src/test/java");
		if(!srcTestRoot.exists()) {
			throw new IllegalStateException("testing folder of source test bench not exists!!!");
		}
		return srcTestRoot;
	}
	
	public SourceFolder getSrcTestSourceFolder() {
		return new SourceFolder(getSrcRootTestFolder(),FiltersFactory.defaultJavaExtensionFilter(),true);
	}

	public Repository getTestingRepository(String startfolder) {
		return
				RepositoryMulpleFolders.newFromSingleSourceRoot(getSrcRootTestFolder(),
				(int level, String path, File file)->path.startsWith(startfolder));
	}
	
	public JavaParser getTestingJavaParser() {
		return JavaParserFactory.newDefaultJavaParser(Arrays.asList(getSrcTestSourceFolder()));
	}
	

}
