package jmdevall.opencodeplan.adapter.out.javaparser;

import java.util.List;

/**
 * Source of compilation units
 * 
 */
public interface CuSource {

	String getSource(String path);

	List<String> getPaths();

}