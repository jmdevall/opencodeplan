package jmdevall.opencodeplan.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Chunk;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;
import com.github.javaparser.JavaParser;

import jmdevall.opencodeplan.adapter.out.javaparser.AstConstructorJavaParser;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSource;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceSingleFile;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.promptmaker.DiffUtil;
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
	
	/**
	 * I have 3 versions of the file: original -> pruned -> revised
	 * 
	 * Finally I found the solution to do the patch as it would be done over the original version:

	 * I replaced the pruned version with a "fake version". I apply manually all the deltas and replace the line with the target.
	 * 
	 * It is possible because the source is only one line and the target usually are more than one line, 
	 * but if I join all the lines of the target in one line, then it respect all line positions of the next patch.
	 *  
	 * @throws PatchFailedException
	 */
	
	@Test
	public void aversiasi() throws PatchFailedException {
		String subpath="/jmdevall/opencodeplan/application/javadiff/mmb/original";
		List<String> original=DiffUtil.tolines(getTestSourceFile(subpath+"/Example.java"));
		List<String> pruned=DiffUtil.tolines(getTestSourceFile(subpath+"/Pruned.javaf"));
		List<String> revised=DiffUtil.tolines(getTestSourceFile(subpath+"/Revised.javaf"));

		//patchPrunedToOriginal should only have deltas of source=1 line and target= multiples lines because It's only method body deletions
		Patch<String> patchPrunedToOriginal=DiffUtils.diff(pruned, original);
		
		Patch<String> patchPrunedToRevised=DiffUtils.diff(pruned, revised);
		
		ArrayList<String> prunedCopy=new ArrayList<String>(pruned);
		for(AbstractDelta<String> delta:patchPrunedToOriginal.getDeltas()) {

			Chunk<String> source=delta.getSource();
			int position=source.getPosition();
			
			//in this line it actually goes more than one line but do not alter the positions for the next patch 
			String multilineHack=delta.getTarget().getLines().stream().collect(Collectors.joining(System.lineSeparator()));
			prunedCopy.remove(position);
			prunedCopy.add(position, multilineHack);
		}
		
		List<String> pruebafinal=DiffUtils.patch(prunedCopy,patchPrunedToRevised);
		
		System.out.println("...and the final result is..............");
		
		System.out.println(pruebafinal
				.stream().collect(Collectors.joining(System.lineSeparator())));
	}
		
}
