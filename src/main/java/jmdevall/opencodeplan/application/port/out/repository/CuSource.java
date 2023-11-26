package jmdevall.opencodeplan.application.port.out.repository;

import java.util.Collection;

public interface CuSource {

	String getSource(String path);

	Collection<String> getPaths();

}