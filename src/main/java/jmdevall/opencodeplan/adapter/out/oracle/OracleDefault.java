package jmdevall.opencodeplan.adapter.out.oracle;

import jmdevall.opencodeplan.domain.DeltaSeeds;
import jmdevall.opencodeplan.port.out.oracle.Oracle;
import jmdevall.opencodeplan.port.out.repository.Repository;

public class OracleDefault implements Oracle {

	
	@Override
	public DeltaSeeds oracle(Repository r) {
		
		//TODO: 
		return new DeltaSeeds(); 
	}

}
