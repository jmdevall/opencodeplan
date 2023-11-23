package jmdevall.opencodeplan.application.port.out.oracle;

import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.instruction.DeltaSeeds;

public interface Oracle {

	DeltaSeeds oracle(Repository r);

}