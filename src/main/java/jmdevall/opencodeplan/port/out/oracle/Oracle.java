package jmdevall.opencodeplan.port.out.oracle;

import jmdevall.opencodeplan.DeltaSeeds;
import jmdevall.opencodeplan.port.out.repository.Repository;

public interface Oracle {

	DeltaSeeds oracle(Repository r);

}