package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
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

	public Range minus(Range absoluteNode) {
		int beglin = absoluteNode.begin.getLine()- this.begin.getLine();
		return Range.newRange(
				beglin, beglin==0?absoluteNode.begin.getColumn()-this.begin.getColumn():absoluteNode.begin.getColumn()
			  , absoluteNode.end.getLine()  - this.begin.getLine(), absoluteNode.end.getColumn() -this.begin.getColumn());
	}
	

}
