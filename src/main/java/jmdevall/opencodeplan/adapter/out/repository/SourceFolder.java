package jmdevall.opencodeplan.adapter.out.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jmdevall.opencodeplan.adapter.out.file.FileUtil;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.DirExplorer;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.Filter;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.CuSourceMulti;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SourceFolder {
	private File sourceRoot;
	private Filter filter;
	boolean testSources;
	
	public boolean contains(String filePath) {
		
		File thefile=new File(sourceRoot,filePath);
		return thefile.exists();
		
	}
	
	public void save(String filepath, String newFileContent) {
		File targetFile = new File(this.sourceRoot, filepath);
		File parent = targetFile.getParentFile();
		if (parent != null && !parent.exists() && !parent.mkdirs()) {
		   throw new IllegalStateException("Couldn't create dir: " + parent);
		}
		try {
			Files.write(targetFile.toPath(), newFileContent.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CuSource getCuSource() {
		CuSourceMulti cuSource=new CuSourceMulti();

        new DirExplorer(
        		filter,
            
            (level, subpathFromRoot, file) -> {
               String javaFileContent= FileUtil.readFile(file.getAbsolutePath()) +"\n"; //bug de javaparser https://github.com/javaparser/javaparser/issues/2169
               cuSource.add(subpathFromRoot, javaFileContent);

         }).explore(sourceRoot);
        
        return cuSource;

	}
}
