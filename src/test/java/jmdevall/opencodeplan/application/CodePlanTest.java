package jmdevall.opencodeplan.application;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.DependencyGraphConstructorJavaparser;
import jmdevall.opencodeplan.adapter.out.javaparser.ParserJavaParser;
import jmdevall.opencodeplan.adapter.out.llm.cache.LlmEngineCacheAdapter;
import jmdevall.opencodeplan.adapter.out.llm.debug.LlmEngineDebugAdapter;
import jmdevall.opencodeplan.adapter.out.llm.ooba.LlmEngineOoba;
import jmdevall.opencodeplan.adapter.out.llm.openai.LlmEngineOpenai;
import jmdevall.opencodeplan.adapter.out.oracle.OracleDefault;
import jmdevall.opencodeplan.adapter.out.repository.FiltersFactory;
import jmdevall.opencodeplan.adapter.out.repository.RepositoryMulpleFolders;
import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.application.port.out.oracle.Oracle;
import jmdevall.opencodeplan.application.port.out.parser.DependencyGraphConstructor;
import jmdevall.opencodeplan.application.port.out.parser.Parser;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.application.port.out.repository.SourceFolder;
import jmdevall.opencodeplan.domain.dependencygraph.LineColPos;
import jmdevall.opencodeplan.domain.instruction.DeltaSeeds;
import jmdevall.opencodeplan.domain.instruction.InstructuionNatural;
import jmdevall.opencodeplan.domain.instruction.NodeSearchDescriptor;
import jmdevall.opencodeplan.domain.instruction.Seed;
import jmdevall.opencodeplan.domain.plangraph.NodeTypeTag;
import jmdevall.opencodeplan.domain.promptmaker.InstructionTemplate;
import jmdevall.opencodeplan.domain.promptmaker.PromptMaker;
import jmdevall.opencodeplan.domain.promptmaker.PromptMakerDefault;

public class CodePlanTest {

	
	@Test
	public void testCodePlan() {
		
		//initialize default dependencies....
		Parser parser=new ParserJavaParser();
		PromptMakerDefault promptMaker=new PromptMakerDefault(InstructionTemplate.CHATML);
		DependencyGraphConstructor dgConstructor=DependencyGraphConstructorJavaparser.newDefault();
		Oracle oracle=new OracleDefault();
		Llm llm=newTestingLlm();
		
		CodePlan sut =new CodePlan(parser, dgConstructor, promptMaker, oracle,llm);

		
		Repository r=RepositoryMulpleFolders.newFromSingleSourceRoot(new File("/home/vicuna/js/nemofinder/src/main/java"));
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
	

	@Test
	public void otroTestCodePlan() {
		
		//initialize default dependencies....
		Parser parser=new ParserJavaParser();
		PromptMakerDefault promptMaker=new PromptMakerDefault(InstructionTemplate.CHATML);
		DependencyGraphConstructor dgConstructor=DependencyGraphConstructorJavaparser.newDefault();
		Oracle oracle=new OracleDefault();
		Llm llm=newTestingLlm();
		
		CodePlan sut =new CodePlan(parser, dgConstructor, promptMaker, oracle,llm);

		Repository r = getNemoFinderProjectRepository();
		DeltaSeeds deltaSeed=new DeltaSeeds();
		
		Seed initialCommand=Seed.builder()
				.instruction(new InstructuionNatural("Haz que el método retorne un Collection<String> en lugar de un List"))
				.block(NodeSearchDescriptor.builder()
						.file("/nemofinder/DictionarySpanish.java")
						.nodeTypeTag(NodeTypeTag.BodyOfMethod)
						.position(LineColPos.newPosition(14, 0))
						.build()
				 )
				.build();
		
		deltaSeed.addSeed(initialCommand);
		
		sut.codePlan(r,deltaSeed);
	}


	private Repository getNemoFinderProjectRepository() {
		SourceFolder srcmain=new SourceFolder(new File("/home/vicuna/js/nemofinder/src/main/java"), FiltersFactory.defaultJavaExtensionFilter(), false);
		SourceFolder srctest=new SourceFolder(new File("/home/vicuna/js/nemofinder/src/test/java"), FiltersFactory.defaultJavaExtensionFilter(), true);
		
		Repository r=RepositoryMulpleFolders.newRepositoryMultipleFolders(Arrays.asList(srcmain,srctest));
		return r;
	}
	

	@Test
	public void otro3TestCodePlan() {
		
		//initialize default dependencies....
		Parser parser=new ParserJavaParser();
		PromptMakerDefault promptMaker=new PromptMakerDefault(InstructionTemplate.ALPACA);
		DependencyGraphConstructor dgConstructor=DependencyGraphConstructorJavaparser.newDefault();
		Oracle oracle=new OracleDefault();
		Llm llm=newTestingLlm();
		
		CodePlan sut =new CodePlan(parser, dgConstructor, promptMaker, oracle,llm);
		
		Repository r=getNemoFinderProjectRepository();

		DeltaSeeds deltaSeed=new DeltaSeeds();
		
		Seed initialCommand=Seed.builder()
				.instruction(new InstructuionNatural("Renombra el método getWords por damePalabras"))
				.block(NodeSearchDescriptor.builder()
						.file("/nemofinder/DictionarySpanish.java")
						.nodeTypeTag(NodeTypeTag.BodyOfMethod)
						.position(LineColPos.newPosition(14, 0))
						.build()
				 )
				.build();
		
		deltaSeed.addSeed(initialCommand);
		
		sut.codePlan(r,deltaSeed);
	}

	
	private Llm newTestingLlm() {
		try {
			
			File cacheFolder=new File("/home/vicuna/.opencodeplan/cache");
			File debugFolder=new File("/home/vicuna/.opencodeplan/debug");
			
			//LlmEngine ooba=new LlmEngineOoba("http://localhost:5000/api");
			LlmEngine ooba=new LlmEngineOpenai("http://localhost:5000", "isthesame", "foo");
			LlmEngine cache=new LlmEngineCacheAdapter(ooba,cacheFolder);
			LlmEngine debug=new LlmEngineDebugAdapter(cache,debugFolder);
			
			Llm llm=new Llm(debug);
			return llm;
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}
}
