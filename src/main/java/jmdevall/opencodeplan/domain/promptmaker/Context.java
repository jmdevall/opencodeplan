package jmdevall.opencodeplan.domain.promptmaker;

import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
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
        // TODO:
        return new Context();
    }
	
}
