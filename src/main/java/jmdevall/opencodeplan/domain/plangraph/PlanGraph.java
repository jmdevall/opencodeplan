package jmdevall.opencodeplan.domain.plangraph;

import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.dependencygraph.Node;

/**
 * a directed acyclic graph with multiple root nodes each node in the graph
 * identifies a code edit obligation that the LLM needs to discharge and an edge
 * indicates that the target node needs to be discharged consequent to the
 * source node.
 */

public class PlanGraph {

	// TODO
	public void addRoot(BI bi, boolean pending) {

	}

	public BI getNextPending() {
		// TODO
		return new BI();
	}

	public boolean hasNodesWithPendingStatus() {
		// TODO:
		return true;
	}

	public void markCompleted(Node b) {
		// TODO:

	}
}