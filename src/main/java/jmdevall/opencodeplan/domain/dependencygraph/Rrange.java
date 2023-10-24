package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Rrange {
	
	private int begin;
	private int end;
	
	public Rrange minus(Rrange r2) {
		return new Rrange(this.begin-r2.begin, this.end-r2.begin);
	}

	@Override
	public String toString() {
		return "[" +begin + "->" + end + "](" + (end-begin) +")";
	}
	
}
