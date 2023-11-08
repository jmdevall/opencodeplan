package jmdevall.opencodeplan.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.github.javaparser.JavaParser;

import jmdevall.opencodeplan.adapter.out.javaparser.AstConstructorJavaParser;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSource;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceSingleFile;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AtomicChange {
	
	public static Node getTestingCu(String filepath) {
		CuSource testingCuSource=new CuSourceSingleFile(filepath, getTestSourceFile(filepath));
				
		AstConstructorJavaParser acjp=new AstConstructorJavaParser(testingCuSource);
		CuSourceProcessor.process(testingCuSource, acjp, new JavaParser());
		Node compilationUnit=acjp.getForest().get(filepath);
		return compilationUnit;
	}
	
	@Test
	public void prueba2() {
		Node cuoriginal=getTestingCu("/jmdevall/opencodeplan/application/javadiff/mmb/original/Example.java");
		String revised=getTestSourceFile("/jmdevall/opencodeplan/application/javadiff/mmb/revised/Example.java");
		
		Patch<String> patch=DiffUtils.diffInline(
				cuoriginal.prompt(),revised);
		
		for(AbstractDelta<String> delta:patch.getDeltas()) {
			int line=delta.getSource().getPosition()+1;
			System.out.println("delta= "+delta);
			
			int dl=0;
			for(String lineString:delta.getSource().getLines()) {
				System.out.println("dl "+(line+dl)+" >"+lineString);
				dl++;
			}
			
			//System.out.println("linea="+line);
			//System.out.println(delta);
			
		}
	}
	
	
	public static String getTestSourceFile(String filepath) {

		File file = new File("src/test/java" + filepath);

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			return reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (FileNotFoundException e) {
			fail();
		} catch (IOException e) {
			fail();
		}
		return null;

	}

	@Test
	public void prueba3() {
		String content=getTestSourceFile("/jmdevall/opencodeplan/domain/foo/Foo.java");
		log.debug("foo"+content);
		assertNotNull(content);
		
	}
		
}
