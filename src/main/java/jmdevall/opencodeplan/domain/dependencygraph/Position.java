package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Position {
	private int line;
	private int column;
}
