package jmdevall.opencodeplan.adapter.out.repository;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jmdevall.opencodeplan.adapter.out.file.direxplorer.Filter;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.application.port.out.repository.SourceFolder;
import jmdevall.opencodeplan.application.port.out.repository.cusource.CuSourceCombined;

public class RepositoryMulpleFolders implements Repository{

	/**
	 * List of sourcefolder in order
	 */
	private List<RepositorySingleFolder> repositories;
	
	private RepositoryMulpleFolders(File srcRoot,Filter filter) {
		super();
		SourceFolder source=new SourceFolder(srcRoot,filter,false);
		RepositorySingleFolder repositorySingleFolder = new RepositorySingleFolder(source);
		this.repositories=Arrays.asList(repositorySingleFolder);
	}
	
	private RepositoryMulpleFolders(List<RepositorySingleFolder> repositories) {
		this.repositories=repositories;
	}
	
	public static RepositoryMulpleFolders newFromSingleSourceRoot(File srcRoot) {
		return new RepositoryMulpleFolders(srcRoot, FiltersFactory.defaultJavaExtensionFilter());
	}
	
	public static RepositoryMulpleFolders newFromSingleSourceRoot(File srcRoot,Filter fileFilter) {
		return new RepositoryMulpleFolders(srcRoot, fileFilter);
	}
	
	public static RepositoryMulpleFolders newRepositoryMultipleFolders(List<SourceFolder> sourceFolders) {
		List<RepositorySingleFolder> repositories=sourceFolders.stream()
				.map(sf->new RepositorySingleFolder(sf))
				.collect(Collectors.toList());
		return new RepositoryMulpleFolders(repositories);
	}
	
	
	@Override
	public List<SourceFolder> getBuildPath() {
		return repositories.stream().map(r->r.getSourceFolder()).collect(Collectors.toList());
	}

	@Override
	public void save(String filepath, String newFileContent) {
		RepositorySingleFolder destSourceFolder=findFirstSourceFolderContainingExistingPath(filepath)
				.orElse(this.repositories.get(0));
		
		destSourceFolder.save(filepath, newFileContent);
	}
	
	private Optional<RepositorySingleFolder> findFirstSourceFolderContainingExistingPath(String filepath) {
		for(RepositorySingleFolder sourceFolder:repositories) {
			if(sourceFolder.contains(filepath)) {
				return Optional.of(sourceFolder);
			}
		}
		return Optional.empty();
	}

	@Override
	public CuSource getCuSource() {
		List<CuSource> cuSources=this.repositories.stream()
				.map(r->r.getCuSource())
				.collect(Collectors.toList());
		
		return new CuSourceCombined(cuSources);
	}


}
