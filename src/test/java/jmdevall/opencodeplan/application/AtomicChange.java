package jmdevall.opencodeplan.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.github.javaparser.JavaParser;

import jmdevall.opencodeplan.adapter.out.javaparser.AstConstructorJavaParser;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSource;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceSingleFile;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.LineColRange;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
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
	
	@Test
	public void prueba2() {
		String original=getTestSourceFile("/jmdevall/opencodeplan/application/javadiff/mmb/original/Example.java");
		String revised=getTestSourceFile("/jmdevall/opencodeplan/application/javadiff/mmb/revised/Example.javaf");

		
		Patch<String> patch=DiffUtils.diff(
				DiffUtil.tolines(original)
				,DiffUtil.tolines(revised));
		
		
		Node cuoriginal=getTestingCu("/jmdevall/opencodeplan/application/javadiff/mmb/original/Example.java");

		NodeId interested=NodeId.builder()
		.file("/jmdevall/opencodeplan/application/javadiff/mmb/original/Example.java")
		.range(LineColRange.newRangeOne(10 ,9))
		.build();
		
		Node prunedcu=Fragment.extractCodeFragment(cuoriginal, Arrays.asList(interested), cuoriginal);

		
		for(AbstractDelta<String> delta:patch.getDeltas()) {
			int line=delta.getSource().getPosition()+1;
			System.out.println("linea "+line+" delta= "+delta);
			
			int dl=0;
			for(String lineString:delta.getSource().getLines()) {
				System.out.println("source "+(line+dl)+" >"+lineString);
				dl++;
			}
			
			dl=0;
			for(String lineString:delta.getTarget().getLines()) {
				System.out.println("target "+(line+dl)+" >"+lineString);
				dl++;
			}
			
			if(delta.getType()==DeltaType.CHANGE) {
				int linea=delta.getSource().getPosition()+1;
				cuoriginal.getId().containsByPosition(interested);
			}
			
			//System.out.println("linea="+line);
			//System.out.println(delta);
			
		}
		
		
		
		//System.out.println(prunedcu.prompt());
		//System.out.println("----");

	}
	
	@Test
	public void conelmetodoRowGenerator() {
		DiffRowGenerator generator = DiffRowGenerator.create()
				.showInlineDiffs(true)
				.inlineDiffByWord(true)
				.oldTag(f -> "~")
				.newTag(f -> "**").build();
		
		Node cuoriginal=getTestingCu("/jmdevall/opencodeplan/application/javadiff/mmb/original/Example.java");
		String revised=getTestSourceFile("/jmdevall/opencodeplan/application/javadiff/mmb/revised/Example.java");
	
		List<DiffRow> rows = generator.generateDiffRows(
				DiffUtil.tolines(cuoriginal.prompt()),
				DiffUtil.tolines(revised));

		System.out.println("|original|new|");
		System.out.println("|--------|---|");
		for (DiffRow row : rows) {
			System.out.println("|" + row.getOldLine() + "|" + row.getNewLine() + "|");
		}
	}
	

	/*
	otra(){
		List<String> unifiedDiff = UnifiedDiffUtils.   generateUnifiedDiff ("original-file.txt", "new-file.txt", text1, diff, 0);

	}
	*/

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
	
	@Test
	public void aversiasi() throws PatchFailedException {
		List<String> original=DiffUtil.tolines(getTestSourceFile("/jmdevall/opencodeplan/application/javadiff/mmb/original/Example.java"));
		List<String> pruned=DiffUtil.tolines(getTestSourceFile("/jmdevall/opencodeplan/application/javadiff/mmb/original/Pruned.javaf"));
		List<String> revised=DiffUtil.tolines(getTestSourceFile("/jmdevall/opencodeplan/application/javadiff/mmb/revised/Example.javaf"));

		Patch<String> patch=DiffUtils.diff(pruned, original);
		
		Patch<String> patchrevised=DiffUtils.diff(pruned, revised);
		
		for(AbstractDelta<String> delta:patchrevised.getDeltas()) {
			patch.addDelta(delta);
			
		}
		
		
		System.out.println(DiffUtils.patch(pruned, patch)
				.stream().collect(Collectors.joining(System.lineSeparator())));
		
		
	}
		
}
