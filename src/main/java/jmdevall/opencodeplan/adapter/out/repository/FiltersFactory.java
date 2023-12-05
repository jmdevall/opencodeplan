package jmdevall.opencodeplan.adapter.out.repository;

import jmdevall.opencodeplan.adapter.out.file.direxplorer.Filter;

public class FiltersFactory {

	public static Filter defaultJavaExtensionFilter() {
		return (level, path, file) -> path.endsWith(".java");
	}
	
	public static Filter allFiles() {
		return (level, path, file) -> true;
	}
}
