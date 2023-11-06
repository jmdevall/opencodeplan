package jmdevall.opencodeplan.application;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.Test;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

public class JavaDiff {

	@Test
	public void nose() {
		DiffRowGenerator generator = DiffRowGenerator.create()
				.showInlineDiffs(true)
				.inlineDiffByWord(true)
				.oldTag(f -> "~")
				.newTag(f -> "**").build();
		
		List<DiffRow> rows = generator.generateDiffRows(
				
				Arrays.asList(
						"This is a test senctence.", 
						"This is the second line.",
						"And here is the finish."),
				
				Arrays.asList("This is a test for diffutils.",
						"This is the second line."));

		System.out.println("|original|new|");
		System.out.println("|--------|---|");
		for (DiffRow row : rows) {
			System.out.println("|" + row.getOldLine() + "|" + row.getNewLine() + "|");
		}
	}

	@Test
	public void otro() {
		
		//create a configured DiffRowGenerator
		DiffRowGenerator generator = DiffRowGenerator.create()
		                .showInlineDiffs(true)
		                .mergeOriginalRevised(true)
		                .inlineDiffByWord(true)
		                .oldTag(f -> "~")      //introduce markdown style for strikethrough
		                .newTag(f -> "**")     //introduce markdown style for bold
		                .build();

		//compute the differences for two test texts.
		List<DiffRow> rows = generator.generateDiffRows(
		                Arrays.asList("This is a test senctence."),
		                Arrays.asList("This is a test for diffutils."));
		        
		System.out.println(rows.get(0).getOldLine());
	}
	
	@Test
	public void otromas() {
		
		
		//build simple lists of the lines of the two testfiles
		//List<String> original = Files.readAllLines(new File(ORIGINAL).toPath());
		//List<String> revised = Files.readAllLines(new File(RIVISED).toPath());

		
		List<String> original=Arrays.asList(
				"This is a test senctence.", 
				"This is the second line.",
				"And here is the finish."),
		
		revised=Arrays.asList("This is a test for diffutils.",
				"This is the second line.");
				
		//compute the patch: this is the diffutils part
		Patch<String> patch = DiffUtils.diff(original, revised);

		//simple output the computed patch to console
		for (AbstractDelta<String> delta : patch.getDeltas()) {
			delta.
			System.out.println("-----");
			System.out.println("changeposition "+delta.getSource().getPosition()+" "+delta.getSource().);
		    System.out.println(delta);
		}
		

	}
	
}
