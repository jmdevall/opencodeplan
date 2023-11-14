package jmdevall.opencodeplan.adapter.out.javaparser.cusource;

import java.util.Collection;
import java.util.HashMap;

/**
 * Source of compilation units
 * 
 */
public class CuSource {
 	private HashMap<String,String> files=new HashMap<String,String>();
	
	public String getSource(String path) {
		return files.get(path);
	}

	public Collection<String> getPaths(){
		return files.keySet();
	}

	public void add(String path,String content){
		this.files.put(path,content);
	}
}