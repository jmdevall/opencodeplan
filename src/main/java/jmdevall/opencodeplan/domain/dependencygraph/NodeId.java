package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
@ToString
public class NodeId {
	private String file;
	private Range range;
}