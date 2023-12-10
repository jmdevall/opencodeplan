package jmdevall.opencodeplan.adapter.out.llm.openai;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.google.gson.Gson;

import jmdevall.opencodeplan.adapter.out.llm.ooba.jsonmodel.Request;
import jmdevall.opencodeplan.adapter.out.llm.ooba.jsonmodel.Response;
import jmdevall.opencodeplan.adapter.out.llm.openai.jsonmodel.ChatRequest;
import jmdevall.opencodeplan.adapter.out.llm.openai.jsonmodel.ChatResponse;
import jmdevall.opencodeplan.application.port.out.llm.LlmEngine;
import jmdevall.opencodeplan.application.port.out.llm.LlmException;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 */
@Slf4j
public class LlmEngineOpenai implements LlmEngine {
	
	private String targetUrl;
	
	private Gson gson;
	private String model;
	private String openApiKey;
	
	public LlmEngineOpenai(String targetUrl,String model,String openApiKey) {
		super();
		this.targetUrl = targetUrl;
		this.model=model;
		this.openApiKey=openApiKey;
		this.gson=new Gson();
	}

	private String getJsonRequest(String prompt) {
		ChatRequest request = new ChatRequest(model, prompt);
		request.setModel(this.model);
		//request.setTemperature(0.0);
		//request.setN(2000);
		String json = gson.toJson(request);
		return json;
	}

	@Override
	public String generate(String prompt) throws LlmException{
		
		
		String bodyRequest=getJsonRequest(prompt); 
		
        try {
			URI targetURI = new URI(targetUrl+"/v1/chat/completions");
			HttpRequest httpRequest = HttpRequest.newBuilder()
			        .uri(targetURI)
			        .POST(HttpRequest.BodyPublishers.ofString(bodyRequest))
			        .header("Content-Type", "application/json")
			        .header("Authorization","Bearer "+this.openApiKey)
			        .timeout(Duration.ofMinutes(50)) //usando un modelo que se ejecuta en CPU los resultados son extremadamente lentos
			        .build();
			
			HttpClient httpClient = HttpClient.newHttpClient();
			
			HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
			        
			String tryParseResponseBody = tryParseResponseBody(response.body());
			System.out.println("salida del llm:\n");
			return tryParseResponseBody;
		} catch (URISyntaxException e) {
			log.error("Uri Syntax Exception",e);
			throw new LlmException(e);
		} catch (IOException e) {
			log.error("IO Exception",e);
			throw new LlmException(e);
		} catch (InterruptedException e) {
			log.error("Interrupted exception",e);
			throw new LlmException(e);
		}
	}

	private String tryParseResponseBody(String json) {
		ChatResponse r=gson.fromJson(json, ChatResponse.class);
		checkResponse(r);
		return r.getChoices().get(0).getMessage().getContent();
	}

	private void checkResponse(ChatResponse r) {
		// TODO: must check if response is ok
	}
	

}
