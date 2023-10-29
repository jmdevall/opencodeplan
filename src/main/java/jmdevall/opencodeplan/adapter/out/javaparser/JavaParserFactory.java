package jmdevall.opencodeplan.adapter.out.javaparser;

import java.io.File;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class JavaParserFactory {

	public static JavaParser newDefaultJavaParser(File rootDir) {
		TypeSolver typeSolver = new CombinedTypeSolver(
	            new ReflectionTypeSolver(),
	            //new JarTypeSolver("jars/library3.jar"),
	            new JavaParserTypeSolver(rootDir)
	            //new JavaParserTypeSolver(new File("generated_code"))
	    );
	 	JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver); 
	
		ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(symbolSolver);
		JavaParser parser=new JavaParser(parserConfiguration);
		return parser;
	}

	public static JavaParser newDefaultJavaParserWithoutTypeSolver() {
		return new JavaParser();
	}
	
}
