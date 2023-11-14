package jmdevall.opencodeplan.adapter.out.javaparser;

import java.io.File;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSource;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CuSourceProcessor {

	public static void process(CuSource cuSource, CuProcessor processor, JavaParser javaparser) {
		for(String filepath:cuSource.getPaths()) {
        	
            ParseResult<CompilationUnit> compilationUnitPr = javaparser.parse(cuSource.getSource(filepath));
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

