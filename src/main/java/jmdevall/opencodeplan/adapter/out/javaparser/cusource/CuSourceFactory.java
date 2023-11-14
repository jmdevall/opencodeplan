package jmdevall.opencodeplan.adapter.out.javaparser.cusource;

import java.io.File;


import jmdevall.opencodeplan.adapter.out.file.FileUtil;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.DirExplorer;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.Filter;
import lombok.extern.slf4j.Slf4j;

/**
 * Source of compilation units that is a simple rootFolder in the system
 * The rootFolder contains the source code.
 * 
 */

@Slf4j
public class CuSourceFactory {
 	
	private static Filter defaultJavaExtensionFilter() {
		return (level, path, file) -> path.endsWith(".java");
	}
	
	public static CuSource newDefaultJavaCuSourceFolder(File rootDir){
        return newFromRootFolderAndFilter(rootDir, defaultJavaExtensionFilter());
	}

	public static CuSource newFromRootFolderAndFilter(File rootDir, Filter defaultJavaExtensionFilter) {
		CuSource cuSource=new CuSource();

        new DirExplorer(
        	defaultJavaExtensionFilter,
            
            (level, subpathFromRoot, file) -> {
               String javaFileContent= FileUtil.readFile(file.getAbsolutePath()) +"\n"; //bug de javaparser https://github.com/javaparser/javaparser/issues/2169
               cuSource.add(subpathFromRoot, javaFileContent);

         }).explore(rootDir);
        
        return cuSource;
	}
	
	public static CuSource newFromSingleFile(String path, String content) {
		CuSource cuSource=new CuSource();
		cuSource.add(path, content);
		return cuSource;
	}
        
}
