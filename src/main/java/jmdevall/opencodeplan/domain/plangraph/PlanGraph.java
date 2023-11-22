package jmdevall.opencodeplan.domain.plangraph;

import java.util.ArrayList;
import java.util.List;
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

	private ArrayList<Obligation> obligationRoots = new ArrayList<Obligation>();

	public void addPendingRoot(BI bi) {
		obligationRoots.add(Obligation.builder().b(bi.getB()).i(bi.getI()).cmi(null).status(Status.PENDING).build());

	}

	public Optional<Obligation> getNextPending() {
		for (Obligation o : obligationRoots) {
			Optional<Obligation> found = o.findNextPendingRecursive();
			if (found.isPresent()) {
				return found;
			}
		}
		return Optional.empty();
	}

	public boolean hasNodesWithPendingStatus() {
		for (Obligation o : obligationRoots) {
			if (o.isPendingRecursive()) {
				return true;
			}
		}
		return false;
	}

	public void markCompleted(Node b) {
		for (Obligation o : obligationRoots) {
			if (o.searchRecursiveToMarkCompleted(b)) {
				return;
			}
		}
	}

	public void addPendingChild(Node parent, Node child, CMIRelation cmi) {
		for (Obligation o : obligationRoots) {
			o.searchRecursiveToAddPendingChild(parent, child, cmi);
		}
	}

	public TemporalContext getTemporalContext(Node targetNode) {
		TemporalContext temporalContext = new TemporalContext();
		   List<Obligation> visitedNodes = new ArrayList<>();

		// Recorrer todos los nodos raíz
		for (Obligation root : obligationRoots) {
			// Llamar a la función recursiva para obtener el contexto temporal
			getTemporalContextRecursive(root, targetNode, temporalContext,visitedNodes);
		}

		return temporalContext;
	}

	private void getTemporalContextRecursive(Obligation currentNode, Node targetNode, TemporalContext temporalContext, List<Obligation> visitedNodes) {
		// Si el nodo actual es el nodo objetivo, agregar el contexto temporal
		if (currentNode.getB().equals(targetNode)) {
			for(Obligation o:visitedNodes) {
				TemporalChange change=TemporalChange.builder()
						.fragment(o.getFragment())
						.cause(o.getCmi())
						.build();
						
				temporalContext.addChange(change);	
			}
		}else {
			visitedNodes.add(currentNode);

			// Recorrer todos los hijos del nodo actual
			for (Obligation child : currentNode.getChildrens()) {
				// Llamar a la función recursiva para los hijos del nodo actual
				getTemporalContextRecursive(child, targetNode, temporalContext, new ArrayList<Obligation>(visitedNodes));
			}
		}

	}

}