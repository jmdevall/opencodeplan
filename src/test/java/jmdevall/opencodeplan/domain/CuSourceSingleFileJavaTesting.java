package jmdevall.opencodeplan.domain;

import java.util.Arrays;
import java.util.List;

import jmdevall.opencodeplan.adapter.out.javaparser.CuSource;

public class CuSourceSingleFileJavaTesting implements CuSource{

	private String path;
	private String content;
	
	public CuSourceSingleFileJavaTesting(String path, String content) {
		this.path=path;
		this.content=content;
	}
	
	@Override
	public String getSource(String path) {
		return this.content;
	}

	@Override
	public List<String> getPaths() {
		return Arrays.asList(path);
	}

}
