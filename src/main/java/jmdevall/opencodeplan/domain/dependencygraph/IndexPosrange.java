package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class IndexPosrange {
	
	private int begin;
	private int end;
	
	public IndexPosrange minus(IndexPosrange r2) {
		return new IndexPosrange(this.begin-r2.begin, this.end-r2.begin);
	}

	@Override
	public String toString() {
		return "[" +begin + "->" + end + "](" + (end-begin) +")";
	}
	
}
