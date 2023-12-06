package jmdevall.opencodeplan.application.port.out.repository.cusource;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jmdevall.opencodeplan.application.port.out.repository.CuSource;

public class CuSourceCombined implements CuSource{
	
	private List<CuSource> cuSources;

	public CuSourceCombined(List<CuSource> cuSources) {
		super();
		this.cuSources = cuSources;
	}

	@Override
	public String getSource(String path) {
		for(CuSource cu:cuSources) {
			String content=cu.getSource(path);
			if(content!=null) {
				return content;
			}
		}
		return null;
	}

	@Override
	public Collection<String> getPaths() {
		HashSet<String> allpaths=new HashSet<String>();
		
		for(CuSource cu:cuSources) {
			allpaths.addAll(cu.getPaths());
		}
		return allpaths;
	}
	
}