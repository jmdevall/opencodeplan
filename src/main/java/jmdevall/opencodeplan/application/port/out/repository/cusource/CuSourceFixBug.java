package jmdevall.opencodeplan.application.port.out.repository.cusource;

import java.util.Collection;

import jmdevall.opencodeplan.application.port.out.repository.CuSource;

public class CuSourceFixBug implements CuSource{

	 private CuSource adaptee;
	 
	
	public CuSourceFixBug(CuSource adaptee) {
		super();
		this.adaptee = adaptee;
	}

	@Override
	public String getSource(String path) {
		String source=adaptee.getSource(path);
		if(source==null) {
			return null;
		}
		return source+"\n"; //bug javaparser https://github.com/javaparser/javaparser/issues/2169
	}

	@Override
	public Collection<String> getPaths() {
		return adaptee.getPaths();
	}

}
