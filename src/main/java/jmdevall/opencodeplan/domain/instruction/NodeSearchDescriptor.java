package jmdevall.opencodeplan.domain.instruction;

import jmdevall.opencodeplan.domain.dependencygraph.LineColPos;
import jmdevall.opencodeplan.domain.dependencygraph.LineColRange;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.plangraph.NodeTypeTag;
import lombok.Builder;
import lombok.Getter;

/**
 * enables search node by position an nodeTypeTag
 */
@Getter
@Builder
public class NodeSearchDescriptor {
	
	private String file;
	private LineColPos position;
	private NodeTypeTag nodeTypeTag;

	public boolean match(Node n){
		if(file!=null) {
			if(!n.getId().getFile().equals(file)) {
				return false;
			}
		}
		if(position!=null) {
			if(!n.getId().getRange().contains(LineColRange.newRangeOne(position.getLine(), position.getColumn()))){
				return false;
			}
		}
		if(n.getNodeTypeTag()==null) {
			return false;
		}
		if(nodeTypeTag!=null) {
			if(!n.getNodeTypeTag().equals(nodeTypeTag)) {
				return false;
			}
			else {
				return true;
			}
		}
		return true;
	}
}
