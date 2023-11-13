package jmdevall.opencodeplan.domain.plangraph;

import jmdevall.opencodeplan.domain.dependencygraph.DependencyLabel;
import lombok.Getter;

@Getter 
public class CMIRelation{
	private WhatD dgc; //¿old dependency graph or new?
	private DependencyLabel dependencyLabel;
	
	public CMIRelation(WhatD dgc, DependencyLabel dependencyLabel) {
		super();
		this.dgc = dgc;
		this.dependencyLabel = dependencyLabel;
	}
}