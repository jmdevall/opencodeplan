package jmdevall.opencodeplan.adapter.out;

import java.io.File;

import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.Node;

public class RepositoryFile implements Repository{

	private File srcRoot;
	
	public RepositoryFile(File srcRoot) {
		super();
		this.srcRoot = srcRoot;
	}

	@Override
	public File getSrcRoot() {
		return this.srcRoot;
	}

	@Override
	public Fragment extractCodeFragment(BI bi) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Repository merge(Fragment newFragment, Node b) {
		// TODO Auto-generated method stub
		return null;
	}

}
