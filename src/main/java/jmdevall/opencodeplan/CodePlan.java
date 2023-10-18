package jmdevall.opencodeplan;

import java.util.Collections;
import java.util.List;

import jmdevall.opencodeplan.CodePlan.BlockRelationPairs;
import jmdevall.opencodeplan.CodePlan.DeltaSeeds;
import jmdevall.opencodeplan.CodePlan.Labels;
import jmdevall.opencodeplan.CodePlan.Llm;
import jmdevall.opencodeplan.CodePlan.Prompt;
import jmdevall.opencodeplan.domain.BlockCode;
import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.I;
import jmdevall.opencodeplan.domain.Label;
import jmdevall.opencodeplan.domain.Node;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.port.out.ConstructDependencyGraph;
import jmdevall.opencodeplan.port.out.repository.Repository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CodePlan {

    class Llm {
        Fragment invoke(Prompt prompt) {
            // TODO:
            return new Fragment();
        }
    }

    class DeltaSeed {

    }

    Node selectOrAddNode(BlockCode b, boolean pending) {
        // TODO:
        //return new Node();
        return null;
    }

    void addEdge(PlanGraph g, Node m, Node n, Relation rel) {
        // TODO:
    }

    class BlockRelationPair {
        public BlockCode b;
        public Relation rel;
    }

    class BlockRelationPairs {
        List<BlockRelationPair> get() {
            return Collections.emptyList();
        }
    }

    BlockRelationPairs getAffectedBlocks(List<Label> labels, BlockCode b, DependencyGraph d, DependencyGraph dp) {
        // TODO:
        return new BlockRelationPairs();
    }

    class Context {

    }

    class Prompt {

    }

    class Labels {

    }

    List<Label> classifyChanges(Fragment fragment, Fragment newFragment) {
        // TODO:
        return Collections.<Label>emptyList();
    }

    Prompt makePrompt(Fragment fragment, I i, Context context) {
        // TODO:
        return new Prompt();
    }

    Context gatherContext(BlockCode b, Repository r, DependencyGraph d) {
        // TODO:
        return new Context();
    }

    // TODO: oracle
    DeltaSeeds oracle(Repository r) {
        return new DeltaSeeds();
    }

    class Relation {

    }

    class DeltaSeeds {
        boolean isEmpty() {
            // TODO:
            return true;
        }

        List<BI> getBIs() {
            // TODO:
            return Collections.emptyList();
        }

    }

    /*
     * Inputs: R is the , Delta_seeds is a set of seed edit
     * specifications, Theta is an oracle and L is an LLM.
     */

    ConstructDependencyGraph constructDependencyGraph;
    
    
/**
 * 
 * @param r source code of a repository
 * @param DeltaSeeds
 * @param theta oracle
 * @param l:llm 
 * 
 * 
 */
    
void codePlan(Repository r, DeltaSeeds deltaSeeds, Llm llm){
    PlanGraph g = new PlanGraph();
    DependencyGraph d = constructDependencyGraph.construct(r);
     while (!deltaSeeds.isEmpty()){
        initializePlanGraph(g, deltaSeeds);
        adaptivePlanAndExecute(r, d, g,llm);
        deltaSeeds = oracle(r);
     }
}

    void initializePlanGraph(PlanGraph g, DeltaSeeds deltaSeeds) {
        deltaSeeds.getBIs().forEach(bi -> {
            g.addRoot(bi, true);
        });
    }

    void adaptivePlanAndExecute(Repository r, DependencyGraph d, PlanGraph g, Llm llm) {
        while (g.hasNodesWithPendingStatus()) {

            BI bi = g.getNextPending();
            // First step: extract fragment of code
            Fragment fragment = r.extractCodeFragment(bi);
            // Second step: gather context of the edit
            Context context = gatherContext(bi.b, r, d);
            // Third step: use the LLM to get edited code fragment
            Prompt prompt = makePrompt(fragment, bi.i, context);
            Fragment newFragment = llm.invoke(prompt);
            // Fourth step: merge the updated code fragment into R

            r = r.merge(newFragment, bi.b);
            List<Label> labels = classifyChanges(fragment, newFragment);
            DependencyGraph dp = d.updateDependencyGraph(labels, fragment, newFragment, bi.b);

            // Fifth step: adaptively plan and propogate the effect of the edit on dependant
            // code
            BlockRelationPairs blockRelationPairs = getAffectedBlocks(labels, bi.b, d, dp);
            g.markCompleted(bi.b);
            blockRelationPairs.get().forEach(br -> {
                Node n = bi.b.getNode();
                Node m = selectOrAddNode(br.b, true);
                addEdge(g, m, n, br.rel);
            });

            d = dp;
        }
    }
}
