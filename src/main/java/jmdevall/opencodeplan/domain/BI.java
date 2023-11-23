package jmdevall.opencodeplan.domain;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.instruction.Instruction;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BI {
    private Node b;
    private Instruction i;
}