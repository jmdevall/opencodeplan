package jmdevall.opencodeplan.adapter.out.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CuSourceProcessor {


	public static void process(CuSource cuSource, CuProcessor processor){
   	 	JavaParser parser = Util.newDefaultJavaParser(cuSource.getSrcRoot());
   	 	
        for(String filepath:cuSource.getPaths()) {
        	
            ParseResult<CompilationUnit> compilationUnitPr = parser.parse(cuSource.getSource(filepath));
            if(compilationUnitPr.isSuccessful()) {
         	   com.github.javaparser.ast.Node rootnode=compilationUnitPr.getResult().get().findRootNode();
         	   CompilationUnit cu=Util.getCompilationUnit(rootnode);
         	   processor.process(cu);
            }else {
         	   //TODO ¿que hago si no compila?
         	   log.error("compilation error!");
            }
        	
        }
	}

}

