package jmdevall.opencodeplan.application.port.out.parser;

import jmdevall.opencodeplan.domain.dependencygraph.Node;

public interface Parser {
	Node parse(String code);
}
