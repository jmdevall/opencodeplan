package jmdevall.opencodeplan.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jmdevall.opencodeplan.application.port.out.oracle.Oracle;
import jmdevall.opencodeplan.application.port.out.parser.ConstructDependencyGraph;
import jmdevall.opencodeplan.application.port.out.parser.Parser;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.BlockRelationPair;
import jmdevall.opencodeplan.domain.DeltaSeeds;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyLabel;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.plangraph.CMI;
import jmdevall.opencodeplan.domain.plangraph.PlanGraph;
import jmdevall.opencodeplan.domain.promptmaker.Context;
import jmdevall.opencodeplan.domain.promptmaker.PromptMaker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CodePlan {

    List<BlockRelationPair> getAffectedBlocks(List<CMI> labels, Node b, DependencyGraph d, DependencyGraph dp) {
    	//TODO:
    	return new ArrayList<BlockRelationPair>();
    }
    
    Parser parser;
   
    private void merge(Repository r,Fragment fragment, String llmrevised, Node b){
    	String curevised=fragment.merge(llmrevised);
    	Node revisedCuNodeParsed=parser.parse(curevised);
    	fragment.setRevised(revisedCuNodeParsed);
    	
    	r.save(b.getId().getFile(), curevised);
    }
    
    /*
     * Inputs: R is the , Delta_seeds is a set of seed edit
     * specifications, Theta is an oracle and L is an LLM.
     */

    ConstructDependencyGraph constructDependencyGraph;
    PromptMaker promptMaker;
    Oracle oracle;
    
	/**
	 * 
	 * @param r source code of a repository
	 * @param DeltaSeeds
	 * @param theta oracle
	 * @param l:llm 
	 * 
	 * 
	 */
	    
	void codePlan(Repository r, DeltaSeeds deltaSeeds, Llm llm, PromptMaker pm){
	    PlanGraph g = new PlanGraph();
	    DependencyGraph d = constructDependencyGraph.construct(r);
	     while (!deltaSeeds.isEmpty()){
	        initializePlanGraph(g, deltaSeeds);
	        adaptivePlanAndExecute(r, d, g,llm);
	        deltaSeeds = oracle.oracle(r);
	     }
	}

    void initializePlanGraph(PlanGraph g, DeltaSeeds deltaSeeds) {
        deltaSeeds.getBIs().forEach(bi -> {
            g.addPendingRoot(bi);
        });
    }

    void adaptivePlanAndExecute(Repository r, DependencyGraph d, PlanGraph g, Llm llm) {
        while (g.hasNodesWithPendingStatus()) {

            BI bi = g.getNextPending().get().getBi();
            // First step: extract fragment of code
            Fragment fragment = r.extractCodeFragment(bi);
            // Second step: gather context of the edit
            Context context = Context.gatherContext(bi.getB(), r, d);
            // Third step: use the LLM to get edited code fragment
            String prompt = promptMaker.makePrompt(fragment, bi.getI(), context);
            String llmrevised = llm.invoke(prompt);
            // Fourth step: merge the updated code fragment into R

            merge(r,fragment, llmrevised, bi.getB());
            
            List<CMI> labels=fragment.classifyChanges();
            
            DependencyGraph dp = d.updateDependencyGraph(labels, fragment, newFragment, bi.getB());

            // Fifth step: adaptively plan and propogate the effect of the edit on dependant
            // code
            List<BlockRelationPair> blockRelationPairs = getAffectedBlocks(labels, bi.getB(), d, dp);
            g.markCompleted(bi.getB());
            
            blockRelationPairs.forEach(br -> {
                Node n = bi.getB();
                g.addPendingChild(n, br.b, br.cmi);
            });

            d = dp;
        }
    }
}
