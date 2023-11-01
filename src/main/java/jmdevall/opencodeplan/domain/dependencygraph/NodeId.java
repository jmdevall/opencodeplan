package jmdevall.opencodeplan.domain.dependencygraph;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
public class NodeId {
	
	@NonNull
	private String file;
	private Range range;
	
	@Override
	public String toString() {
		return "NodeId(file=" + file + ", range=" + range + ")";
	}
	
	public boolean isSameFile(NodeId other){
		return (this.getFile().equals(other.getFile()));
	}
	
	
}