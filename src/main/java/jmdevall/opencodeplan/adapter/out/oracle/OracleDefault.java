package jmdevall.opencodeplan.adapter.out.oracle;

import jmdevall.opencodeplan.application.port.out.oracle.Oracle;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.instruction.DeltaSeeds;

public class OracleDefault implements Oracle {

	
	@Override
	public DeltaSeeds oracle(Repository r) {
		
		//TODO: 
		return new DeltaSeeds(); 
	}

}
