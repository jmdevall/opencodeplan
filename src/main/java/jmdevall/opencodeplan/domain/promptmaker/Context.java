package jmdevall.opencodeplan.domain.promptmaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.dependencygraph.Rel;
import jmdevall.opencodeplan.port.out.repository.Repository;

/**
 * The context of the edit
(line 38–41) consists of (a) spatial context, which contains related code such as methods
called from the block 𝐵, and 
(b) temporal context, which contains the previous edits that
caused the need to edit the block 𝐵. The temporal context is formed by edits along the paths
from the root nodes of the plan graph to 𝐵.
 */
public class Context {

	//SpatialContext
	
	public static Context gatherContext(Node b, Repository r, DependencyGraph d) {
		/*
		Spatial context in CodePlan refers to the arrangement and relationships of
		code blocks within a codebase, helping understand how classes, functions, variables, and modules
		are structured and interact. It’s crucial for making accurate code changes. CodePlan utilizes the
		dependency graph to extract spatial context, representing code as nodes and their relationships
		as edges. This graph enables CodePlan to navigate codebases, identify relevant code blocks, and
		maintain awareness of their spatial context. As a result, when generating code edits, the dependency
		graph empowers CodePlan to make context-aware code modifications that are consistent with the
		code’s spatial organization, enhancing the accuracy and reliability of its code editing capabilities.
		*/
		List<String> spatialContext=new ArrayList<String>();
		
		//d. findByNo>deId(b);
		Optional<Node> found=d.findFinalNodeContaining(b.getId());
		Node node=found.get();
		
		//find the method
		Node method=node.getRootParent().toStream()
				.filter(n->n.isMethodContaining(node)).findFirst().get();
		
		List<Rel> rels=d.getRels();
		/*
		for(Rel rel:rels) {
			if(method.getId().getRange().contains(rel.getOrigin().getRange())) {
				Node nodeRelacionado=d.findByNodeId(rel.getDestiny()).get();
				spatialContext.add(
						"El método está relacionado con otra parte del código del proyecto:\n"
						+ " tipo de relacion=" + rel.getLabel()
						+ " con este código: \n"
						+ "```java\n"+nodeRelacionado.getContent()+"\n```");
			}
		}*/
		
		List<Node> affectedBlocks=new ArrayList<Node>();
		
		for(Rel rel:rels) {
			if(method.getId().getRange().contains(rel.getOrigin().getRange())) {
				Node nodeRelacionado=d.findByNodeId(rel.getDestiny()).get();
				affectedBlocks.add(nodeRelacionado);
			}
		}

		Stream<Node> affectedCus=affectedBlocks.stream()
			.map(n->n.getId().getFile())
			.distinct()
			.map( file ->d.getForest().get(file));
				
		List<Node> fragmentos=affectedCus
				.map(cu->Node.extractCodeFragment(cu, affectedBlocks, null))
				.collect(Collectors.toList());
			
		

		fragmentos.forEach(n->System.out.println("codigo relacionado:" + n.prompt()));
		
		/*for(String cosa:spatialContext) {
		/*	System.out.println(cosa);
		/*}
		
		
		/*
		The plan graph records all change obligations and their inter-dependences.
		Extracting temporal context is accomplished by linearizing all paths from the root nodes of the plan
		graph to the target node. Each change is a pair of the code fragments before and after the change.
		The temporal context also states the "causes" (recorded as edge labels) that connect the target node
		with its predecessor nodes. For example, if a node A is connected to B with a CalledBy edge, then
		the temporal context for B is the before/after fragments for A and a statement that says that "B
		calls A", which helps the LLM understand the cause-effect relation between the latest temporal
		change (change to A) and the current obligation (to make a change to B).*/
		
		// TODO:
        return new Context();
        
        
    }
	
}
