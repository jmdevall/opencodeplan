package jmdevall.opencodeplan.domain.plangraph;

import java.util.ArrayList;
import java.util.Optional;

import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.dependencygraph.Node;

/**
 * a directed acyclic graph with multiple root nodes each node in the graph
 * identifies a code edit obligation that the LLM needs to discharge and an edge
 * indicates that the target node needs to be discharged consequent to the
 * source node.
 */

public class PlanGraph {

	private ArrayList<Obligation> obligationRoots=new ArrayList<Obligation>();
	
	public void addPendingRoot(BI bi) {
		obligationRoots.add(
				Obligation.builder()
				.b(bi.getB())
				.i(bi.getI())
				.cmi(null)
				.status(Status.PENDING)
				.build());
				
	}

	public Optional<Obligation> getNextPending() {
		for(Obligation o:obligationRoots) {
			Optional<Obligation> found=o.findNextPendingRecursive();
			if(found.isPresent()) {
				return found;
			}
		}
		return Optional.empty();
	}

	public boolean hasNodesWithPendingStatus() {
		for(Obligation o:obligationRoots) {
			if(o.isPendingRecursive()) {
				return true;
			}
		}
		return false;
	}

	public void markCompleted(Node b) {
		for(Obligation o:obligationRoots) {
			if(o.searchRecursiveToMarkCompleted(b)) {
				return;
			}
		}
	}

}