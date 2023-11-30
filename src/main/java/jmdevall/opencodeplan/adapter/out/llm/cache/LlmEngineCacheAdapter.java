package jmdevall.opencodeplan.adapter.out.llm.cache;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.application.port.out.llm.LlmException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LlmEngineCacheAdapter implements LlmEngine{
	
	private LlmEngine llmEngine;
	private File cacheFolder;
	
	private static final int PADDING=5;
	private static final String REQUEST_EXTENSION=".req";
	private static final String RESPONSE_EXTENSION=".res";
	
	public LlmEngineCacheAdapter(LlmEngine llmEngine, File cacheFolder){
		this.llmEngine=llmEngine;
		this.cacheFolder=cacheFolder;
		
		log.info("llmEngine will cache llm requests and responses in folder: "+cacheFolder.getAbsolutePath());
	}


	@Override
	public String generate(String prompt) throws LlmException {
	
		try {
			String md5sum = Md5Util.md5sum(prompt);
			
			final String filenameRequest = md5sum + REQUEST_EXTENSION;
			final String filenameResponse = md5sum + RESPONSE_EXTENSION;

			final File fileRequest = new File(cacheFolder, filenameRequest);
			Files.writeString(fileRequest.toPath(),prompt, StandardOpenOption.CREATE);
			
			final File fileResponse = new File(cacheFolder, filenameResponse);
			if(fileResponse.exists()) {
				return Files.readString(fileResponse.toPath());
			}
			else {
				String response=this.llmEngine.generate(prompt);
				Files.writeString(fileResponse.toPath(), response, StandardOpenOption.CREATE);
				return response;
			}
		} catch (IOException e) {
			throw new LlmException(e);
		}
	}
	
	

}
