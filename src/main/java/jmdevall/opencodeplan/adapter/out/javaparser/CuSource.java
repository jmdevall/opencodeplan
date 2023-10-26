package jmdevall.opencodeplan.adapter.out.javaparser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jmdevall.opencodeplan.adapter.out.file.FileUtil;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.DirExplorer;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.Filter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CuSource {
	

 	private HashMap<String,String> files=new HashMap<String,String>();
	private File rootDir;

 	private CuSource(File rootDir,HashMap<String,String> files) {
 		this.files=files;
 		this.rootDir=rootDir;
 	}
 	
	private static Filter defaultJavaExtensionFilter() {
		return (level, path, file) -> path.endsWith(".java");
	}
	
	public static CuSource newFromFile(File rootDir){

        return newFromFileAndFilter(rootDir, defaultJavaExtensionFilter());
	}

	public static CuSource newFromFileAndFilter(File rootDir, Filter defaultJavaExtensionFilter) {
		HashMap<String,String> readedfiles=new HashMap<String,String>();

        new DirExplorer(
        	defaultJavaExtensionFilter,
            
            (level, subpathFromRoot, file) -> {
               String javaFileContent= FileUtil.readFile(file.getAbsolutePath()) +"\n"; //bug de javaparser https://github.com/javaparser/javaparser/issues/2169
               readedfiles.put(subpathFromRoot, javaFileContent);

         }).explore(rootDir);
        
        
        return new CuSource(rootDir,readedfiles);
	}
	
	public File getSrcRoot() {
		return this.rootDir;
	}
	
	public String getSource(String path) {
		return files.get(path);
	}
	
	public List<String> getPaths(){
		return new ArrayList<String>(this.files.keySet());
	}
        
	
	

}
