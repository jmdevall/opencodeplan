package jmdevall.opencodeplan.adapter.in.commandline;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import jmdevall.opencodeplan.adapter.out.javaparser.DependencyGraphConstructorJavaparser;
import jmdevall.opencodeplan.adapter.out.javaparser.ParserJavaParser;
import jmdevall.opencodeplan.adapter.out.llm.cache.LlmEngineCacheAdapter;
import jmdevall.opencodeplan.adapter.out.llm.debug.LlmEngineDebugAdapter;
import jmdevall.opencodeplan.adapter.out.llm.ooba.LlmEngineOoba;
import jmdevall.opencodeplan.adapter.out.llm.openai.LlmEngineOpenai;
import jmdevall.opencodeplan.adapter.out.oracle.OracleDefault;
import jmdevall.opencodeplan.adapter.out.repository.FiltersFactory;
import jmdevall.opencodeplan.adapter.out.repository.RepositoryMulpleFolders;
import jmdevall.opencodeplan.application.CodePlan;
import jmdevall.opencodeplan.application.Llm;
import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.application.port.out.oracle.Oracle;
import jmdevall.opencodeplan.application.port.out.parser.DependencyGraphConstructor;
import jmdevall.opencodeplan.application.port.out.parser.Parser;
import jmdevall.opencodeplan.application.port.out.repository.Repository;
import jmdevall.opencodeplan.application.port.out.repository.SourceFolder;
import jmdevall.opencodeplan.domain.promptmaker.PromptMakerDefault;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenCodeplanFactory {
	
	public static CodePlan newFromProperties(Properties properties) throws IOException {
		String llmengineType=properties.getProperty("llm.engine.type").trim();
		
		LlmEngine engine=getLlm(properties, llmengineType);
		
		Parser parser=new ParserJavaParser();
		PromptMakerDefault promptMaker=new PromptMakerDefault();
		DependencyGraphConstructor dgConstructor=DependencyGraphConstructorJavaparser.newDefault();
		Oracle oracle=new OracleDefault();
		Llm llm=new Llm(engine);
		CodePlan codeplan = new CodePlan(parser, dgConstructor, promptMaker, oracle,llm);
		return codeplan;
	}

	public static Repository getDefaultMavenProjectFromCurrentFolder() {
		File currentDirectory = new File(".");
		
		SourceFolder srcmain=new SourceFolder(new File(currentDirectory,"/src/main/java"), FiltersFactory.defaultJavaExtensionFilter(), false);
		SourceFolder srctest=new SourceFolder(new File(currentDirectory,"/src/test/java"), FiltersFactory.defaultJavaExtensionFilter(), true);
		
		Repository r=RepositoryMulpleFolders.newRepositoryMultipleFolders(Arrays.asList(srcmain,srctest));
		return r;
	}

	private static LlmEngine getLlm(Properties properties, String llmengineType) throws IOException {
		LlmEngine engine;
		if("ooba".equals(llmengineType)) {
			engine=new LlmEngineOoba(properties.getProperty("llm.engine.ooba.url"));
			log.info("using llm type=ooba");
		}else if("openai".equals(llmengineType)) {
			engine=new LlmEngineOpenai(
					properties.getProperty("llm.engine.openai.url").trim(),
					properties.getProperty("llm.engine.openai.model").trim(),
					properties.getProperty("llm.engine.openai.key").trim()
			);
			log.info("using llm type=openai");
		}else {
			throw new IllegalStateException("llm.engine.type not valid");
		}
		
		boolean cacheactive="true".equals(properties.getProperty("llm.cache.active"));
		if(cacheactive) {

			String llmCacheFolder=properties.getProperty("llm.cache.folder",null);
			
			File theDir = llmCacheFolder==null?new File(System.getProperty("user.home"),".opencodeplan/cache"): new File(llmCacheFolder);
			
			if (!theDir.exists()){
			    theDir.mkdirs();
			}
			log.info("using cache folder "+llmCacheFolder);
			
			
			engine=new LlmEngineCacheAdapter(engine, theDir);
		}
		
		boolean debugActive="true".equals(properties.getProperty("llm.debug.active"));
		if(debugActive) {
			String llmDebugFolder=properties.getProperty("llm.debug.folder",null);
			
			File theDir = llmDebugFolder==null?new File(System.getProperty("user.home"),".opencodeplan/debug"): new File(llmDebugFolder);
			
			if (!theDir.exists()){
			    theDir.mkdirs();
			}
			log.info("using llmDebugFolderr "+llmDebugFolder);
			
			
			engine=new LlmEngineDebugAdapter(engine, theDir);
		}
		return engine;
	}
	
}
