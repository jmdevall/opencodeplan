package jmdevall.opencodeplan.adapter.out.file.direxplorer;

import java.io.File;

public class DirExplorer {

	private FileHandler fileHandler;
	private Filter filter;

	public DirExplorer(Filter filter, FileHandler fileHandler) {
		this.filter = filter;
		this.fileHandler = fileHandler;
	}

	public void explore(File root) {
		explore(0, "", root);
	}

	private void explore(int level, String path, File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				explore(level + 1, path + "/" + child.getName(), child); //TODO: windows? 
			}
		} else {
			if (filter.interested(level, path, file)) {
				fileHandler.handle(level, path, file);
			}
		}
	}
}