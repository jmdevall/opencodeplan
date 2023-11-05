package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.List;

import jmdevall.opencodeplan.domain.dependencygraph.DependencyRelation;

public class LogRelUtil {

	public static void logRels(List<DependencyRelation> rels) {
		for(DependencyRelation rel:rels) {
			System.out.println("rel="+rel);
		}
	}

}
