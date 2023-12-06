package jmdevall.opencodeplan.adapter.out.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import jmdevall.opencodeplan.adapter.out.file.FileUtil;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.DirExplorer;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.application.port.out.repository.SourceFolder;
import jmdevall.opencodeplan.application.port.out.repository.cusource.CuSourceMulti;
import lombok.Getter;

@Getter
public class RepositorySingleFolder implements Repository{

	private SourceFolder sourceFolder;
	
	public RepositorySingleFolder(SourceFolder sourceFolder) {
		this.sourceFolder=sourceFolder;
	}
	
	public boolean contains(String filePath) {
		File thefile=new File(this.sourceFolder.getSourceRoot(),filePath);
		return thefile.exists();
	}
	
	@Override
	public void save(String filepath, String newFileContent) {
		File targetFile = new File(this.sourceFolder.getSourceRoot(), filepath);
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
	
	@Override
	public CuSource getCuSource() {
		CuSourceMulti cuSource=new CuSourceMulti();

        new DirExplorer(
        		this.sourceFolder.getFilter(),
            
            (level, subpathFromRoot, file) -> {
               String javaFileContent= FileUtil.readFile(file.getAbsolutePath()) +"\n"; //bug de javaparser https://github.com/javaparser/javaparser/issues/2169
               cuSource.add(subpathFromRoot, javaFileContent);

         }).explore(this.getSourceFolder().getSourceRoot());
        
        return cuSource;

	}

	@Override
	public List<SourceFolder> getBuildPath() {
		return Arrays.asList(this.sourceFolder);
	}
	
	
}
