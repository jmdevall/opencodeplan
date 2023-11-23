package jmdevall.opencodeplan.application;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.DependencyGraphConstructorJavaparser;
import jmdevall.opencodeplan.adapter.out.javaparser.ParserJavaParser;
import jmdevall.opencodeplan.adapter.out.llm.ooba.LlmEngineOoba;
import jmdevall.opencodeplan.adapter.out.oracle.OracleDefault;
import jmdevall.opencodeplan.adapter.out.repository.RepositoryFile;
import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.application.port.out.oracle.Oracle;
import jmdevall.opencodeplan.application.port.out.parser.DependencyGraphConstructor;
import jmdevall.opencodeplan.application.port.out.parser.Parser;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.domain.dependencygraph.LineColPos;
import jmdevall.opencodeplan.domain.instruction.DeltaSeeds;
import jmdevall.opencodeplan.domain.instruction.InstructuionNatural;
import jmdevall.opencodeplan.domain.instruction.NodeSearchDescriptor;
import jmdevall.opencodeplan.domain.instruction.Seed;
import jmdevall.opencodeplan.domain.plangraph.NodeTypeTag;
import jmdevall.opencodeplan.domain.promptmaker.PromptMaker;
import jmdevall.opencodeplan.domain.promptmaker.PromptMakerDefault;

public class CodePlanTest {

	@Test
	public void testCodePlan() {
		
		//initialize default dependencies....
		Parser parser=new ParserJavaParser();
		PromptMakerDefault promptMaker=new PromptMakerDefault();
		DependencyGraphConstructor dgConstructor=DependencyGraphConstructorJavaparser.newDefault();
		Oracle oracle=new OracleDefault();
		Llm llm=newTestingLlm();
		
		CodePlan sut =new CodePlan(parser, dgConstructor, promptMaker, oracle,llm);

		
		Repository r=RepositoryFile.newRepositoryFile(new File("/home/vicuna/js/nemofinder/src/main/java"));
		DeltaSeeds deltaSeed=new DeltaSeeds();
		
		Seed initialCommand=Seed.builder()
				.instruction(new InstructuionNatural("Convierte el HashMap<String, List<String>> en un HashMap que tenga como clave Integer en lugar de String"))
				.block(NodeSearchDescriptor.builder()
						.file("/nemofinder/HerigoneSpanish.java")
						.nodeTypeTag(NodeTypeTag.BodyOfMethod)
						.position(LineColPos.newPosition(41, 0))
						.build()
				 )
				.build();
		
		deltaSeed.addSeed(initialCommand);
		
		sut.codePlan(r,deltaSeed);
	}
	
	private Llm newTestingLlm() {
		LlmEngine engine=new LlmEngineOoba("http://localhost:5000/api");
		Llm llm=new Llm(engine);
		return llm;
	}
}
