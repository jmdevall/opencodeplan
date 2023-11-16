package jmdevall.opencodeplan.application.port.out.repository;

import java.io.File;

public interface Repository {

	File getSrcRoot();
	
	CuSource getCuSource();

    void save(String filepath, String newFileContent);
}