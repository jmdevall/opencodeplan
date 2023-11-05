package jmdevall.opencodeplan.application.port.out;

import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;

public interface ConstructDependencyGraph {
	DependencyGraph construct(Repository repository);
}
