package jmdevall.opencodeplan.application.port.out.repository;

import java.util.Arrays;
import java.util.Collection;

public class CuSourceSingle implements CuSource {

	private String content;
	private String path;
	
	public CuSourceSingle(String path,String content) {
		super();
		this.content = content;
		this.path = path;
	}

	@Override
	public String getSource(String path) {
		return content;
	}

	@Override
	public Collection<String> getPaths() {
		return Arrays.asList(path);
	}

}
