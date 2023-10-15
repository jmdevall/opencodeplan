package jmdevall.opencodeplan.adapter.out.javaparser;

import java.io.File;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import jmdevall.opencodeplan.adapter.out.file.FileUtil;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.DirExplorer;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.Filter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CuExplorer {
	
	private CuProcessor processor;
	private Filter filter;
	
	public CuExplorer(CuProcessor processor){
		this.processor=processor;
		this.filter=defaultJavaExtensionFilter();
	}

	public CuExplorer(CuProcessor processor,Filter filter){
		this.processor=processor;
		this.filter=filter;
	}
	
	private Filter defaultJavaExtensionFilter() {
		return (level, path, file) -> path.endsWith(".java");
	}
	
	
	
	public void explore(File rootDir){
   	 	JavaParser parser = Util.newDefaultJavaParser(rootDir);
    	
        new DirExplorer(
            filter,
            
            (level, subpathFromRoot, file) -> {
               String javaFileContent= FileUtil.readFile(file.getAbsolutePath());
               
               ParseResult<CompilationUnit> compilationUnitPr = parser.parse(javaFileContent);
               if(compilationUnitPr.isSuccessful()) {
            	   com.github.javaparser.ast.Node rootnode=compilationUnitPr.getResult().get().findRootNode();
            	   CompilationUnit cu=Util.getCompilationUnit(rootnode);
            	   processor.process(cu);
               }else {
            	   //TODO ¿que hago si no compila?
            	   log.error("compilation error!");
               }
         }).explore(rootDir);
        
	}

}
