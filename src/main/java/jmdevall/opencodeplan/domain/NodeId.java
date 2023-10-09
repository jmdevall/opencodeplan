package jmdevall.opencodeplan.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class NodeId {
	private String file;
	private Position begin;
	private Position end;
}