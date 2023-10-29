package jmdevall.opencodeplan.adapter.out.javaparser;

import java.util.List;

public interface CuSource {

	String getSource(String path);

	List<String> getPaths();

}