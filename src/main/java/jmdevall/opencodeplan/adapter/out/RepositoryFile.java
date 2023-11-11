package jmdevall.opencodeplan.adapter.out;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
	public void save(String filepath, String newFileContent) {
		File targetFile = new File(srcRoot, filepath);
		File parent = targetFile.getParentFile();
		if (parent != null && !parent.exists() && !parent.mkdirs()) {
		   throw new IllegalStateException("Couldn't create dir: " + parent);
		}
		try {
			Files.write(targetFile.toPath(), newFileContent.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
