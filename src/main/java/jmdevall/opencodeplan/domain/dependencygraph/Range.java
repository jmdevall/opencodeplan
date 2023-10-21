package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Range {
	private Position begin;
	private Position end;
	
	public static Range newRange(int beglin, int begcol, int endlin, int endcol) {
		return Range.builder()
				.begin(Position.builder()
						.line(beglin)
						.column(begcol)
						.build()
					)
				.end(Position.builder()
						.line(endlin)
						.column(endcol)
						.build())
				.build();
	}
	
	@Override
	public String toString() {
		return String.format("[%d,%d]->[%d,%d]",
				this.getBegin().getLine(),this.getBegin().getColumn()
			,this.getEnd().getLine(),this.getEnd().getColumn());
	}

	public boolean contains(Range r) {
		
        return Position.comparator().compare(this.begin, r.begin) <= 0 
        		&& Position.comparator().compare(this.end, r.end) >= 0;
	}
	

}
