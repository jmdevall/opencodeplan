package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
public class NodeId {
	
	private String file;
	private Range range;
	
	@Override
	public String toString() {
		return "NodeId(file=" + file + ", range=" + range + ")";
	}
	
	
}