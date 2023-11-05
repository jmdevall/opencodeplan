package jmdevall.opencodeplan.adapter.out.javaparser.cusource;

import java.util.Arrays;
import java.util.List;

public class CuSourceSingleFile implements CuSource{

	private String path;
	private String content;
	
	public CuSourceSingleFile(String path, String content) {
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
