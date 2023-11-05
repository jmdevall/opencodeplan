package jmdevall.opencodeplan.application;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

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
				Arrays.asList("This is a test senctence.", "This is the second line.", "And here is the finish."),
				Arrays.asList("This is a test for diffutils.", "This is the second line."));

		System.out.println("|original|new|");
		System.out.println("|--------|---|");
		for (DiffRow row : rows) {
			System.out.println("|" + row.getOldLine() + "|" + row.getNewLine() + "|");
		}
	}
}
