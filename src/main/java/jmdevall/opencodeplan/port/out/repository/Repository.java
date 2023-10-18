package jmdevall.opencodeplan.port.out.repository;

import java.io.File;

import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.BlockCode;
import jmdevall.opencodeplan.domain.Fragment;

public interface Repository {

	File getSrcRoot();
        
    Fragment extractCodeFragment(BI bi);

    Repository merge(Fragment newFragment, BlockCode b);

}