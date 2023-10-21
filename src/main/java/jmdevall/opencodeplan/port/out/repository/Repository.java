package jmdevall.opencodeplan.port.out.repository;

import java.io.File;

import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.Node;

public interface Repository {

	File getSrcRoot();
        
    Fragment extractCodeFragment(BI bi);

    Repository merge(Fragment newFragment, Node b);


}