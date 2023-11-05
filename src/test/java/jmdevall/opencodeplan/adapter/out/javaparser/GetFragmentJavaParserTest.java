package jmdevall.opencodeplan.adapter.out.javaparser;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.domain.Fragment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetFragmentJavaParserTest {

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
	
	@Test
	public void canObtainFragmentFromSimpleCode() {
		GetFragmentJavaParser sut=new GetFragmentJavaParser();
		
		Fragment fragment=sut.getFragment(javaCompileUnit);
		fragment.getNode().debugRecursive(0);
	}
	
	
	private static final String nonCompletedCu=
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
	public void canObtainFragmentOfNonCompletedCode() {
		GetFragmentJavaParser sut=new GetFragmentJavaParser();
		
		Fragment fragment=sut.getFragment(nonCompletedCu);
		fragment.getNode().debugRecursive(0);
		
		System.out.println("prompt=....");
		System.out.println(fragment.getNode().prompt());
		
	}
	
}
