package jmdevall.opencodeplan.adapter.out.javaparser;

import com.github.javaparser.ast.Node;

import jmdevall.opencodeplan.domain.NodeId;
import jmdevall.opencodeplan.domain.Position;

public class Util {

	public static Position toDomainPosition( com.github.javaparser.Position position) {
		return Position.builder()
		.line(position.line)
		.column(position.column)
		.build();
	}

	public static NodeId toNodeId(com.github.javaparser.ast.Node node, String file) {
		return NodeId.builder()
			.file(file)
			.begin(toDomainPosition(node.getBegin().get()))
			.end(toDomainPosition(node.getEnd().get()))
			.build();
	}

}
