package jmdevall.opencodeplan.domain;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.plangraph.CMI;
import jmdevall.opencodeplan.domain.plangraph.CMIRelation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockRelationPair {
    public Node b;
    public CMIRelation impact;
}