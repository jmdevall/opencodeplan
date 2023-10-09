package jmdevall.opencodeplan;

import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.BlockCode;

/**
     * a directed acyclic graph with multiple root nodes
     * each node in the graph identifies a code edit obligation that the LLM needs 
     * to discharge and an edge indicates
     * that the target node needs to be discharged consequent to the source node.
     */

   public class PlanGraph {

        // TODO
        void addRoot(BI bi, boolean pending) {

        }

        BI getNextPending() {
            // TODO
            return new BI();
        }

        boolean hasNodesWithPendingStatus() {
            // TODO:
            return true;
        }

        void markCompleted(BlockCode b) {
            // TODO:

        }
    }