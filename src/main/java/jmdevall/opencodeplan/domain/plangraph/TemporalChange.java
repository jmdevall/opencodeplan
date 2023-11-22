package jmdevall.opencodeplan.domain.plangraph;

import jmdevall.opencodeplan.domain.Fragment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TemporalChange {
	private Fragment fragment;
	private CMIRelation cause;
	
}
