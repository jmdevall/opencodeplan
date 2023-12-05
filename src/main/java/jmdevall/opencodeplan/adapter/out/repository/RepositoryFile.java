package jmdevall.opencodeplan.adapter.out.repository;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jmdevall.opencodeplan.adapter.out.file.direxplorer.Filter;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.Repository;

public class RepositoryFile implements Repository{

	/**
	 * List of sourcefolder in order
	 */
	private List<SourceFolder> buildPath;
	
	private RepositoryFile(File srcRoot,Filter filter) {
		super();
		this.buildPath=Arrays.asList(new SourceFolder(srcRoot,filter,false));
	}
	
	public static RepositoryFile newRepositoryFile(File srcRoot) {
		return new RepositoryFile(srcRoot, FiltersFactory.defaultJavaExtensionFilter());
	}
	
	public static RepositoryFile newRepositoryFile(File srcRoot,Filter fileFilter) {
		return new RepositoryFile(srcRoot, fileFilter);
	}

	@Override
	public File getSrcRoot() {
		return this.buildPath.get(0).getSourceRoot();
	}

	@Override
	public void save(String filepath, String newFileContent) {
		SourceFolder destSourceFolder=findFirstSourceFolderContainingExistingPath(filepath)
				.orElse(this.buildPath.get(0));
		
		destSourceFolder.save(filepath, newFileContent);
	}
	
	private Optional<SourceFolder> findFirstSourceFolderContainingExistingPath(String filepath) {
		for(SourceFolder sourceFolder:buildPath) {
			if(sourceFolder.contains(filepath)) {
				return Optional.of(sourceFolder);
			}
		}
		return Optional.empty();
	}

	@Override
	public CuSource getCuSource() {
		//TODO: explore multiples sourcefolders
		return this.buildPath.get(0).getCuSource();
	}

}
