package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class LineColRange {
	private LineColPos begin;
	private LineColPos end;
	
	public static LineColRange newRange(int beglin, int begcol, int endlin, int endcol) {
		return LineColRange.builder()
				.begin(LineColPos.builder()
						.line(beglin)
						.column(begcol)
						.build()
					)
				.end(LineColPos.builder()
						.line(endlin)
						.column(endcol)
						.build())
				.build();
	}
	
	public static LineColRange newRangeOne(int begendlin,int begendcol) {
		return newRange(begendlin,begendcol,begendlin,begendcol);
	}
	
	@Override
	public String toString() {
		return String.format("[%d,%d]->[%d,%d]",
				this.getBegin().getLine(),this.getBegin().getColumn()
			,this.getEnd().getLine(),this.getEnd().getColumn());
	}

	public boolean containsLine(int line) {
		return this.getBegin().getLine()>=line && this.getEnd().getLine()<=line;
	}
	
	public boolean contains(LineColRange r) {
		
        return LineColPos.comparator().compare(this.begin, r.begin) <= 0 
        		&& LineColPos.comparator().compare(this.end, r.end) >= 0;
	}

	public LineColRange minus(LineColRange absoluteNode) {
		int beglin = absoluteNode.begin.getLine()- this.begin.getLine();
		return LineColRange.newRange(
				beglin, beglin==0?absoluteNode.begin.getColumn()-this.begin.getColumn():absoluteNode.begin.getColumn()
			  , absoluteNode.end.getLine()  - this.begin.getLine(), absoluteNode.end.getColumn() -this.begin.getColumn());
	}
	

}
