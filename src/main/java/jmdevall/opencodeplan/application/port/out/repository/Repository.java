package jmdevall.opencodeplan.application.port.out.repository;

import java.io.File;

import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.Node;

public interface Repository {

	File getSrcRoot();
        
    Fragment extractCodeFragment(BI bi);

    void save(String filepath, String newFileContent);
}