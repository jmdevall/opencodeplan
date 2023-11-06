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
			System.out.println(delta.getSource().getChangePosition());
			//DiffUtils.
			//delta.
			//System.out.println("-----");
			//System.out.println("changeposition "+delta.getSource().getPosition()+" "+delta.getSource().);
		    System.out.println(delta);
		    
		}
		

	}
	
	
	  public static	String javaCompileUnit=
			  "package test;\n"                               //1
			+ "\n"                                            //2
			+ "public class Foo{\n"                           //3
			+ "   public void hello(String who){\n"           //4
			+ "       System.out.println(\"hello \"+who);\n"  //5
			+ "   }\n"                                        //6
			+ "\n"                                            //7
			+ "   public void addnumbers(int a, int b){\n"    //8
			+ "      return a+b;\n"                           //9
			+ "   }\n"                                        //10
			+ "   int a;"                                     //11
			+ "}\n";
				
				String expected=
			 "package test;\n"                               //1
			+ "\n"                                            //2
			+ "public class Foo{\n"                           //3
			+ "   public void hello(String who){\n"           //4
			+ "       System.out.println(\"hello \"+who);\n"  //5
			+ "   }\n"
			+ "\n"
			+ "   public void addnumbers(int a, int b)\n"
			+ "   int a;"                                     //11
			+ "}\n";
	
	@Test
	public void nose3() {
		Patch<String> patch=DiffUtils.diffInline(javaCompileUnit, expected);
		for (AbstractDelta<String> delta : patch.getDeltas()) {
		    System.out.println(delta);

		}
	}
	
	@Test

	public void nose4() {
		Patch<String> patch=DiffUtils.diff(
		Arrays.asList(javaCompileUnit.split("\n")),
        Arrays.asList(expected.split("\n")));
		for (AbstractDelta<String> delta : patch.getDeltas()) {
		    System.out.println(delta);

		}
		
	}
	
	@Test
	public void nose5() {
		//create a configured DiffRowGenerator
		DiffRowGenerator generator = DiffRowGenerator.create()
				//.ignoreWhiteSpaces(true)
		                .showInlineDiffs(true)
		                .mergeOriginalRevised(true)
		                .inlineDiffByWord(true)
		                .oldTag(f -> "~")      //introduce markdown style for strikethrough
		                .newTag(f -> "**")     //introduce markdown style for bold
		                .build();

		//compute the differences for two test texts.
		List<DiffRow> rows = generator.generateDiffRows(
						Arrays.asList(javaCompileUnit.split("\n")),
		                Arrays.asList(expected.split("\n")));
		        
		System.out.println("|original|new|");
		System.out.println("|--------|---|");
		for (DiffRow row : rows) {
			System.out.println("|" + row.getOldLine() + "|" + row.getNewLine() + "|");
			
		}
	}
	
}
