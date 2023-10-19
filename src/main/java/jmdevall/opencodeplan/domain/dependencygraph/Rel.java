package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Rel {
	NodeId origin;
	NodeId destiny;
	Label label;
}
