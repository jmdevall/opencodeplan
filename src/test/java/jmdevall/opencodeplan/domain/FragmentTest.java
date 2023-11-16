package jmdevall.opencodeplan.domain;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.github.javaparser.JavaParser;

import jmdevall.opencodeplan.adapter.out.javaparser.AstConstructorJavaParser;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;
import jmdevall.opencodeplan.adapter.out.repository.CuSourceFactory;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FragmentTest {
	
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


	private Node getTestingCu() {
		CuSource testingCuSource=CuSourceFactory.newFromSingleFile("/test/Foo.java", javaCompileUnit);
				
		AstConstructorJavaParser acjp=new AstConstructorJavaParser(testingCuSource);
		CuSourceProcessor.process(testingCuSource, acjp, new JavaParser());
		Node compilationUnit=acjp.getForest().get("/test/Foo.java");
		return compilationUnit;
	}
	
	@Test
	public void prueba() {
		Node cu=getTestingCu();
		
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


	
}
