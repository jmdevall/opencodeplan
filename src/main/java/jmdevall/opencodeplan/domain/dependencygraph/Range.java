package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Range {
	private Position begin;
	private Position end;
	@Override
	public String toString() {
		return String.format("[%d,%d]->[%d,%d]",
				this.getBegin().getLine(),this.getBegin().getColumn()
			,this.getEnd().getLine(),this.getEnd().getColumn());
	}
	

}
