package jmdevall.opencodeplan.application.port.out.repository;

import java.io.File;

import jmdevall.opencodeplan.adapter.out.file.direxplorer.Filter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SourceFolder {
	private File sourceRoot;
	private Filter filter;
	boolean testSource;
}
