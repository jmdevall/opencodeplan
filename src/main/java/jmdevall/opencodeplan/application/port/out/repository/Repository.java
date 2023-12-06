package jmdevall.opencodeplan.application.port.out.repository;

import java.util.List;

public interface Repository {

	List<SourceFolder> getBuildPath();
	
	CuSource getCuSource();

    void save(String filepath, String newFileContent);
}