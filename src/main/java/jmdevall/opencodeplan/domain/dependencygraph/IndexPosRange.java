package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IndexPosRange {
	
	private int begin;
	private int end;
	
	public IndexPosRange minus(IndexPosRange r2) {
		return new IndexPosRange(this.begin-r2.begin, this.end-r2.begin);
	}

	@Override
	public String toString() {
		return "[" +begin + "->" + end + "](" + (end-begin) +")";
	}
	
}
