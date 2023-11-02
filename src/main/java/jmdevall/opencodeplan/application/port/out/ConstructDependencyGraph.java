package jmdevall.opencodeplan.application.port.out;

import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.port.out.repository.Repository;

public interface ConstructDependencyGraph {
	DependencyGraph construct(Repository repository);
}
