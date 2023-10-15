package jmdevall.opencodeplan.adapter.out.javaparser;

import com.github.javaparser.ast.CompilationUnit;

public interface CuProcessor {
	void process(CompilationUnit cu);
}
