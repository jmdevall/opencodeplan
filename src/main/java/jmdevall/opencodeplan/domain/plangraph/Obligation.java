package jmdevall.opencodeplan.domain.plangraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.dependencygraph.Node;
import jmdevall.opencodeplan.domain.instruction.I;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class Obligation {
	Node b;
	I i;
	
	@Setter
	Fragment fragment;
	Status status;
	CMIRelation cmi;
	
	@Builder.Default
	List<Obligation> childrens=new ArrayList<Obligation>();
	
	public void addChildren(Obligation o) {
		this.childrens.add(o);
	}

	void setCompleted(){
		this.status=Status.COMPLETED;
	}
	
	public BI getBi() {
		return BI.builder()
				.b(b)
				.i(i)
				.build();
	}
	
	public boolean isPending() {
		return this.status==Status.PENDING;
	}
	
	public Optional<Obligation> findNextPendingRecursive() {
		if(this.isPending()) {
			return Optional.of(this);
		}
		for(Obligation o:childrens) {
			Optional<Obligation> optionalOfChildren=o.findNextPendingRecursive();
			if(optionalOfChildren.isPresent()) {
				return optionalOfChildren;
			}
		}
		return Optional.empty();
	}
	
	public boolean isPendingRecursive() {
		if(this.isPending()) {
			return true;
		}
		for(Obligation o:childrens) {
			if(o.isPendingRecursive()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param b
	 * @return true if found anywhere in children
	 */
	public boolean searchRecursiveToMarkCompleted(Node b) {
		if(this.getB().equals(b)) {
			if(this.isPending()) {
				this.setCompleted();
				return true;
			}
		}
		else {
			for(Obligation o:childrens) {
				if(o.searchRecursiveToMarkCompleted(b)) {
					return true;
				}
			}
		}
		return false;
	}

	public void searchRecursiveToAddPendingChild(Node parent, Node child, CMIRelation elcmi) {
		if(this.b==parent) {
			Obligation o=
					Obligation.builder()
					.b(child)
					.i(null)
					.cmi(elcmi)
					.status(Status.PENDING)
					.build();
		
			this.childrens.add(o);
		}
		for(Obligation o:this.childrens) {
			o.searchRecursiveToAddPendingChild(parent,child,elcmi);
		}
		
	}
	
	
}
