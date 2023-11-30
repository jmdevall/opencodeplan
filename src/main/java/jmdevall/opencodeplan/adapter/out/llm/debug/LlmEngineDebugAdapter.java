package jmdevall.opencodeplan.adapter.out.llm.debug;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import jmdevall.opencodeplan.adapter.out.llm.cache.Md5Util;
import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.application.port.out.llm.LlmException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LlmEngineDebugAdapter implements LlmEngine{
	
	private LlmEngine llmEngine;
	private File tempFolder;
	private int numRequest=0;
	
	private static final int PADDING=5;
	private static final String EXTENSION=".txt";
	
	public LlmEngineDebugAdapter(LlmEngine llmEngine) throws IOException {
		this.llmEngine=llmEngine;
		this.tempFolder = Files.createTempDirectory("llmengine").toFile();
		String tmpFolderLocation = tempFolder.getAbsolutePath();
		//String tmpDirsLocation = System.getProperty("java.io.tmpdir");
		
		log.info("llmEngine will debug llm conversation to temp folder: "+tmpFolderLocation);
	}

	@Override
	public String generate(String prompt) throws LlmException {
	
		try {
			final String filename = String.format("%0"+PADDING+"d", numRequest)+EXTENSION;

			final File file = new File(tempFolder, filename);
			file.createNewFile(); 
			
			Files.writeString(file.toPath(),getInfo("request to llm",prompt),StandardOpenOption.APPEND);
			Files.writeString(file.toPath(),prompt,StandardOpenOption.APPEND);

			String generated = llmEngine.generate(prompt);

			Files.writeString(file.toPath(),getInfo("response from llm",prompt),StandardOpenOption.APPEND);
			Files.writeString(file.toPath(),generated,StandardOpenOption.APPEND);
			numRequest++;
			return generated;
		} catch (IOException e) {
			throw new LlmException(e);
		}
	}
	
	private String getInfo(String type,String prompt) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb=new StringBuffer();
		sb.append("============================================\n");
		sb.append("type="+type+"\n");
		sb.append(String.format("current Time: "+sdf.format(new Date()))+"\n");
		sb.append("md5="+Md5Util.md5sum(prompt)+"\n");
		
		sb.append("============================================\n");
		return sb.toString();
		
	}
	

}
