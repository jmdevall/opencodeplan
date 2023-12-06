package jmdevall.opencodeplan.application.port.out.repository.cusource;

import java.util.Arrays;
import java.util.Collection;

import jmdevall.opencodeplan.application.port.out.repository.CuSource;

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
