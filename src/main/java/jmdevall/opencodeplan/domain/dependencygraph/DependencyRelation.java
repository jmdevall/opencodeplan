package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DependencyRelation {
	NodeId origin;
	NodeId destiny;
	DependencyLabel label;
	@Override
	public String toString() {
		return "Rel(origin=" + origin + ", destiny=" + destiny + ", label=" + label + ")";
	}
}
