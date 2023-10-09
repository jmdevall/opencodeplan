package jmdevall.opencodeplan.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Position {
	private int line;
	private int column;
}
