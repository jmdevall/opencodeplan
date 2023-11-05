package jmdevall.opencodeplan.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.javaparser.JavaParser;

import jmdevall.opencodeplan.adapter.out.javaparser.AstConstructorJavaParser;
import jmdevall.opencodeplan.adapter.out.javaparser.CuSourceProcessor;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSource;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceSingleFile;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.dependencygraph.LineColRange;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FragmentTest {
	
	String javaCompileUnit=
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
	public void eliminaBloquesDeMetodosNoAfectados() {
		CuSource testingCuSource=new CuSourceSingleFile("/test/Foo.java", javaCompileUnit);
				
		AstConstructorJavaParser acjp=new AstConstructorJavaParser(testingCuSource);
		CuSourceProcessor.process(testingCuSource, acjp, new JavaParser());
		Node compilationUnit=acjp.getForest().get("/test/Foo.java");
		
		Node sentencia=Node.builder()
				.id( NodeId.builder()
						.file("/test/Foo.java")
						.range(LineColRange.newRange(5, 1, 5, 6)).build()).build();
		Fragment f=Fragment.newFragment(compilationUnit, sentencia);
		String prompt = f.getNode().prompt();
		log.debug("resultado fragmento="+prompt);
		assertEquals(expected,prompt);
	}
			
}
