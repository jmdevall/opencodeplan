package jmdevall.opencodeplan.domain.instruction;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Seed {
    private NodeSearchDescriptor block;
    private Instruction instruction;
}
