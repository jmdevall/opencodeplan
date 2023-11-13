package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.List;

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
	private LineColRange range;
	
	@Override
	public String toString() {
		return "NodeId(file=" + file + ", range=" + range + ")";
	}
	
	public boolean isSameFile(NodeId other){
		return (this.getFile().equals(other.getFile()));
	}
	
	public boolean containsByPosition(NodeId other) {
		return this.isSameFile(other) 
				&& 
				this.getRange().contains(other.getRange());
	}
	
	public boolean containsByPosition(List<NodeId> others){
		for(NodeId nodeid:others) {
			if(this.containsByPosition(nodeid)) {
				return true;
			}
		}
		return false;
	}
	

	
	
}