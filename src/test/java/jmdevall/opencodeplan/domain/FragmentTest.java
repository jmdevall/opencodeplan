package jmdevall.opencodeplan.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.github.javaparser.JavaParser;

import jmdevall.opencodeplan.adapter.out.javaparser.AstConstructorJavaParser;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.cusource.CuSourceFactory;
import jmdevall.opencodeplan.domain.dependencygraph.LineColRange;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.plangraph.ClasifiedChange;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FragmentTest {
	
  public static	String originalCu=
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
+ "   int a;\n"                                     //11
+ "}\n";
	
	String revised=
 "package test;\n"                               //1
+ "\n"                                            //2
+ "public class Foo{\n"                           //3
+ "   public void hello(String who){\n"           //4
+ "       System.out.println(\"hello \"+who);\n"  //5
+ "   }\n"
+ "\n"
+ "   public void addnumbers(int a, int b){"
+ "       return a-b;\n"
+ "   }\n"
+ "    \n"
+ "   int a;\n"                                     //11
+ "}\n";


	private Node getTestingCu(String javaFilePath, String javaCompilationUnitContent) {
		CuSource testingCuSource=CuSourceFactory.newFromSingleFile(javaFilePath, javaCompilationUnitContent);
				
		AstConstructorJavaParser acjp=new AstConstructorJavaParser(testingCuSource);
		CuSourceProcessor.process(testingCuSource, acjp, new JavaParser());
		Node compilationUnit=acjp.getForest().get(javaFilePath);
		return compilationUnit;
	}
	
	@Test
	public void prueba() {
		Node cu=getTestingCu("/test/Foo.java",originalCu);
		
		String prompt=cu.prompt();
		
		Patch<String> patch=DiffUtils.diff(
				Arrays.asList(prompt.split("\n")), 
				Arrays.asList(revised.split("\n")));
		
		for(AbstractDelta<String> delta:patch.getDeltas()) {
			int line=delta.getSource().getPosition()+1;
			
			int dl=0;
			for(String lineString:delta.getSource().getLines()) {
				System.out.println("dl "+(line+dl)+" >"+lineString);
				dl++;
			}
			System.out.println("linea="+line);
			System.out.println(delta);
		}
	}
	
	TestingUtil testingUtil=new TestingUtil();


	@Test
	public void merge() {
		Node original=getTestingCu("/test/Foo.java",originalCu);
		
		Fragment sut=Fragment.newFromCuNode(original);
		
		String merged=sut.merge(revised);
		
		assertEquals(revised,merged);
	}
	
	@Test
	public void getClasifiedChanges() {
		Node original=getTestingCu("/test/Foo.java",originalCu);
		Fragment sut=Fragment.newFromCuNode(original);
		
		Node revised=getTestingCu("/test/Foo.java",this.revised);
		sut.setRevised(revised);
		
		List<ClasifiedChange> changes = sut.classifyChanges();
		
		System.out.println("changes="+changes);
		
		

	}
	
	@Test
	public void casoQueFalla() {
		Node original=getTestingCu("/nemofinder/HerigoneSpanish.java",testingUtil.readFileFromTestSource("/caso/original.txt"));
		NodeId afected=NodeId.builder()
				.file("/nemofinder/HerigoneSpanish.java")
				.range(LineColRange.newRangeOne(41, 0))
				.build();
		String revised=testingUtil.readFileFromTestSource("/caso/revised.txt");
		Fragment sut=Fragment.newFromPrunedCuNode(original,afected);
		sut.merge(revised);
		
		
		
	}
	
	
	
}
