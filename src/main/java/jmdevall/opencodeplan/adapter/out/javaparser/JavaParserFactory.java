package jmdevall.opencodeplan.adapter.out.javaparser;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import jmdevall.opencodeplan.application.port.out.repository.SourceFolder;

public class JavaParserFactory {

	public static JavaParser newDefaultJavaParser(List<SourceFolder> sourceFolders) {
		
		//create list of typesolvers from sourcefolders
		List<TypeSolver> typeSolvers=new ArrayList<TypeSolver>();
		typeSolvers.add(new ReflectionTypeSolver());
		for(SourceFolder sourcefolder:sourceFolders) {
			typeSolvers.add(new JavaParserTypeSolver(sourcefolder.getSourceRoot()));
		}
		//others types that could be added....
        //new JarTypeSolver("jars/library3.jar"),
        //new JavaParserTypeSolver(new File("generated_code"))
		
		TypeSolver typeSolver = new CombinedTypeSolver(typeSolvers);

		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver); 
	
		ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(symbolSolver);
		JavaParser parser=new JavaParser(parserConfiguration);
		return parser;
	}

	public static JavaParser newDefaultJavaParserWithoutTypeSolver() {
		return new JavaParser();
	}
	
}
