package jmdevall.opencodeplan.adapter.out.javaparser.relfinders;

import java.util.List;

import jmdevall.opencodeplan.domain.Rel;

public class LogRelUtil {

	public static void logRels(List<Rel> rels) {
		for(Rel rel:rels) {
			System.out.println("rel="+rel);
		}
	}

}
