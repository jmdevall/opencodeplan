package jmdevall.opencodeplan.domain.plangraph;

import java.util.List;

import jmdevall.opencodeplan.domain.dependencygraph.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClasifiedChange {
	CMI cmi;
	List<Node> original;
	List<Node> revised;
}
