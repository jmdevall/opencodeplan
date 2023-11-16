package jmdevall.opencodeplan.adapter.out.repository;

import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import lombok.extern.slf4j.Slf4j;

/**
 * Source of compilation units that is a simple rootFolder in the system
 * The rootFolder contains the source code.
 * 
 */

@Slf4j
public class CuSourceFactory {
	
	public static CuSource newFromSingleFile(String path, String content) {
		CuSource cuSource=new CuSource();
		cuSource.add(path, content);
		return cuSource;
	}
        
}
