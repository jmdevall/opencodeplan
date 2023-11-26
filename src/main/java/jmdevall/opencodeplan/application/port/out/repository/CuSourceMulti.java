package jmdevall.opencodeplan.application.port.out.repository;

import java.util.Collection;
import java.util.HashMap;

/**
 * Source of compilation units
 * 
 */
public class CuSourceMulti implements CuSource {
 	private HashMap<String,String> files=new HashMap<String,String>();
	
	@Override
	public String getSource(String path) {
		return files.get(path);
	}

	@Override
	public Collection<String> getPaths(){
		return files.keySet();
	}

	public void add(String path,String content){
		this.files.put(path,content);
	}
}