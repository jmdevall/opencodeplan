package jmdevall.opencodeplan.adapter.out.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.adapter.out.file.FileUtil;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.DirExplorer;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.Filter;

public class RepositoryFile implements Repository{

	private File srcRoot;
	private Filter filter;
	
	private RepositoryFile(File srcRoot,Filter filter) {
		super();
		this.srcRoot = srcRoot;
		this.filter = filter;
	}
	
	public static RepositoryFile newRepositoryFile(File srcRoot) {
		return new RepositoryFile(srcRoot, defaultJavaExtensionFilter());
	}
	
	public static RepositoryFile newRepositoryFile(File srcRoot,Filter fileFilter) {
		return new RepositoryFile(srcRoot, fileFilter);
	}

	public static Filter defaultJavaExtensionFilter() {
		return (level, path, file) -> path.endsWith(".java");
	}
	
	@Override
	public File getSrcRoot() {
		return this.srcRoot;
	}

	@Override
	public void save(String filepath, String newFileContent) {
		File targetFile = new File(srcRoot, filepath);
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
		CuSource cuSource=new CuSource();

        new DirExplorer(
        		filter,
            
            (level, subpathFromRoot, file) -> {
               String javaFileContent= FileUtil.readFile(file.getAbsolutePath()) +"\n"; //bug de javaparser https://github.com/javaparser/javaparser/issues/2169
               cuSource.add(subpathFromRoot, javaFileContent);

         }).explore(srcRoot);
        
        return cuSource;

	}

}
