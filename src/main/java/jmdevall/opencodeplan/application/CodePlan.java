package jmdevall.opencodeplan.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jmdevall.opencodeplan.application.port.out.oracle.Oracle;
import jmdevall.opencodeplan.application.port.out.parser.DependencyGraphConstructor;
import jmdevall.opencodeplan.application.port.out.parser.Parser;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.BlockRelationPair;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.instruction.DeltaSeeds;
import jmdevall.opencodeplan.domain.plangraph.CMIRelation;
import jmdevall.opencodeplan.domain.plangraph.ClasifiedChange;
import jmdevall.opencodeplan.domain.plangraph.Obligation;
import jmdevall.opencodeplan.domain.plangraph.PlanGraph;
import jmdevall.opencodeplan.domain.plangraph.WhatD;
import jmdevall.opencodeplan.domain.promptmaker.Context;
import jmdevall.opencodeplan.domain.promptmaker.PromptMaker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CodePlan {

    private Parser parser;
    private DependencyGraphConstructor dependencyGraphConstructor;
    private PromptMaker promptMaker;
    private Oracle oracle;
    private Llm llm;
    
    
    List<BlockRelationPair> getAffectedBlocks(List<ClasifiedChange> labels, Node b, DependencyGraph d, DependencyGraph dp) {
    	ArrayList<BlockRelationPair>ret = new ArrayList<BlockRelationPair>();
    	
    	for(ClasifiedChange change:labels) {
    		List<CMIRelation> impacts=change.getCmi().getFormalChangeMayImpact();
    		for(CMIRelation impact:impacts) {
    			DependencyGraph affectedDg=impact.getDgc()==WhatD.D?d:dp;
    			List<Node> afectedNodes=impact.getDgc()==WhatD.D?change.getOriginal():change.getRevised();
    			
    			Node afectedNode=afectedNodes.get(0);

				
				//TODO: Rel tiene nodeids, no nodes. Comparar o buscar los nodos en el dependency graph de otro forma
    			Stream<DependencyRelation> filter = affectedDg.getRels().stream()          
				.filter(rel-> rel.getLabel()==impact.getDependencyLabel())
				//.filter(rel-> rel.getOrigin().equals(b))
				.filter(rel-> rel.getDestiny().equals(afectedNode));
    			
    			List<DependencyRelation> debug=filter.collect(Collectors.toList());
    			
				List<BlockRelationPair> collect = filter
				.map((rel)->{
					Optional<Node> node=affectedDg.findByNodeId(rel.getDestiny());
					if(node.isPresent()) {
						return new BlockRelationPair(node.get(),impact);
					}
					else throw new IllegalStateException();
					
				})
				.collect(Collectors.toList());
    			
				ret.addAll(
	    			collect
	    		);
    		}
    		
    	}
    	return ret;
    }
    
   
   
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

	/**
	 * 
	 * @param r source code of a repository
	 * @param DeltaSeeds
	 * @param theta oracle
	 * @param l:llm 
	 * 
	 * 
	 */
	    
	void codePlan(Repository r, DeltaSeeds deltaSeeds){
	    PlanGraph g = new PlanGraph();
	    DependencyGraph d = dependencyGraphConstructor.constructDependencyGraph(r);
	     while (!deltaSeeds.isEmpty()){
	        initializePlanGraph(g, deltaSeeds, d);
	        adaptivePlanAndExecute(r, d, g,llm);
	        deltaSeeds = oracle.oracle(r);
	     }
	}

    void initializePlanGraph(PlanGraph g, DeltaSeeds deltaSeeds, DependencyGraph d) {
        List<BI> bis = deltaSeeds.getBis(d);
		bis.forEach(bi -> {
            g.addPendingRoot(bi);
        });
    }

    void adaptivePlanAndExecute(Repository r, DependencyGraph d, PlanGraph g, Llm llm) {
        while (g.hasNodesWithPendingStatus()) {

            Obligation obligation = g.getNextPending().get();
			BI bi = obligation.getBi();
            // First step: extract fragment of code
            //Fragment fragment = Fragment.newFromPrunedCuNode( bi.getB().getRootParent(), bi.getB().getId());
			Fragment fragment = Fragment.newFromCuNode(bi.getB().getRootParent());
			
            // Second step: gather context of the edit
            Context context = Context.gatherContext(bi.getB(), r, d ,g);
            // Third step: use the LLM to get edited code fragment
            String prompt = promptMaker.makePrompt(fragment, bi.getI(), context);
            String llmrevised = llm.invoke(prompt);
            // Fourth step: merge the updated code fragment into R

            merge(r,fragment, llmrevised, bi.getB());
            obligation.setFragment(fragment); // este paso no lo pone el paper, pero es la única forma que veo para poder obtener luego los cambios.
            
            
            List<ClasifiedChange> labels=fragment.classifyChanges();
  
//          DependencyGraph dp = d.updateDependencyGraph(labels, fragment, newFragment, bi.getB());
            DependencyGraph dp = dependencyGraphConstructor.constructDependencyGraph(r); //isn't it more easy???

            // Fifth step: adaptively plan and propogate the effect of the edit on dependant
            // code
            List<BlockRelationPair> blockRelationPairs = getAffectedBlocks(labels, bi.getB(), d, dp);
            g.markCompleted(bi.getB());
            
            blockRelationPairs.forEach(br -> {
                Node n = bi.getB();
                g.addPendingChild(n, br.b, br.impact);
            });

            d = dp;
        }
    }

}
