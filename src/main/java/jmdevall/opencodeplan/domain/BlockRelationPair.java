package jmdevall.opencodeplan.domain;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.plangraph.CMI;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockRelationPair {
    public Node b;
    public CMI cmi;
}