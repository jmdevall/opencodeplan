package jmdevall.opencodeplan.adapter.out.llm.ooba;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;


public class LlmOoba {
	
	private String targetUrl;
	
	private Gson gson;
	
	public LlmOoba(String targetUrl) {
		super();
		this.targetUrl = targetUrl;
		this.gson=new Gson();
	}

	private String getJsonRequest(String prompt) {
		Request r=Request.builder()
				.prompt(prompt)
				.auto_max_new_tokens(false)
				.max_new_tokens(2000)
				.max_tokens_seconds(0)
				.build();
		return gson.toJson(r);
	}

	public String generate(String prompt) throws Exception{

		String bodyRequest=getJsonRequest(prompt);
		
        URI targetURI = new URI(targetUrl+"/v1/generate");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(targetURI)
                .POST(HttpRequest.BodyPublishers.ofString(bodyRequest))
                .header("Content-Type", "application/json")
                .build();
        
        HttpClient httpClient = HttpClient.newHttpClient();
        
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                
        return tryParseResponseBody(response.body());
	}

	private String tryParseResponseBody(String json) {
		Response r=gson.fromJson(json, Response.class);
		checkResponse(r);
		return r.getResults().get(0).getText();
	}

	private void checkResponse(Response r) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	/**
	 * 
	 * 
	 * 
conda activate textgen
cd text-generation-webui
export ROC_ENABLE_PRE_VEGA=1
export HIP_VISIBLE_DEVICES=0
python server.py --model phind-codellama-34b-v2.Q5_K_M.gguf --threads 12 --n_ctx 16384 --extensions openai --verbose

https://huggingface.co/TheBloke/Mistral-7B-codealpaca-lora-GGUF
python server.py --model mistral-7b-codealpaca-lora.Q4_K_M.gguf --threads 4 --n_ctx 32768 --api --verbose

python server.py --model mistral-7b-codealpaca-lora.Q4_K_M.gguf --threads 4 --n_ctx 32768 --extensions openai --verbose
---------------prompt de phind
### System Prompt
You are an intelligent programming assistant.

### User Message
Implement a linked list in C++

### Assistant
	 * 
	 */
	
/*
 * 
 * export const fetchGenerate = async function (prompt:string):Promise<string> {

  const response = await fetch('http://localhost:5000/api/v1/generate',{
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        'prompt': prompt,
        'max_new_tokens': 250,
        'auto_max_new_tokens': false,
        'max_tokens_second': 0,

        //# Generation params. If 'preset' is set to different than 'None', the values
        //# in presets/preset-name.yaml are used instead of the individual numbers.
        'preset': 'None',
        'do_sample': true,
        'temperature': 0.7,
        'top_p': 0.1,
        'typical_p': 1,
        'epsilon_cutoff': 0,  //# In units of 1e-4
        'eta_cutoff': 0,  //# In units of 1e-4
        'tfs': 1,
        'top_a': 0,
        'repetition_penalty': 1.18,
        'repetition_penalty_range': 0,
        'top_k': 40,
        'min_length': 0,
        'no_repeat_ngram_size': 0,
        'num_beams': 1,
        'penalty_alpha': 0,
        'length_penalty': 1,
        'early_stopping': false,
        'mirostat_mode': 0,
        'mirostat_tau': 5,
        'mirostat_eta': 0.1,
        'guidance_scale': 1,
        'negative_prompt': '',

        'seed': -1,
        'add_bos_token': true,
        'truncation_length': 2048,
        'ban_eos_token': false,
        'custom_token_bans': '',
        'skip_special_tokens': true,
        'stopping_strings': []
    })

  })

  const data = await response.json();
  if (response.ok) {
    const results = data?.results
    if (results) {
      return results[0].text
      // add fetchedAt helper (used in the UI to help differentiate requests)
      //pokemon.fetchedAt = formatDate(new Date())
      //return pokemon
    } else {
      //return Promise.reject(new Error(`No pokemon with the name "${name}"`))
    }
  } else {
    // handle the graphql errors
    //const error = new Error(errors?.map(e => e.message).join('\n') ?? 'unknown')
    //return Promise.reject(error)
  }
};


 
export const chat = async ()=>{
    return await fetch(
    'http://localhost:5000/api/v1/chat',
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        'user_input': 'la capital de España es ' ,//user_input,
        'max_new_tokens': 250,
        'auto_max_new_tokens': false,
        'max_tokens_second': 0,
        'history': history,
        'mode': 'instruct',  // Valid options: 'chat', 'chat-instruct', 'instruct'
        'character': 'Example',
        'instruction_template': 'Vicuna-v1.1',  //# Will get autodetected if unset
        'your_name': 'You',
        //# 'name1': 'name of user', # Optional
        //# 'name2': 'name of character', # Optional
        //# 'context': 'character context', # Optional
        //# 'greeting': 'greeting', # Optional
        //# 'name1_instruct': 'You', # Optional
        //# 'name2_instruct': 'Assistant', # Optional
        //# 'context_instruct': 'context_instruct', # Optional
        //# 'turn_template': 'turn_template', # Optional
        'regenerate': false,
        '_continue': false,
        'chat_instruct_command': 'Continue the chat dialogue below. Write a single reply for the character "<|character|>".\n\n<|prompt|>',

        //# Generation params. If 'preset' is set to different than 'None', the values
        //# in presets/preset-name.yaml are used instead of the individual numbers.
        'preset': 'None',
        'do_sample': true,
        'temperature': 0.7,
        'top_p': 0.1,
        'typical_p': 1,
        'epsilon_cutoff': 0,  //# In units of 1e-4
        'eta_cutoff': 0,  //# In units of 1e-4
        'tfs': 1,
        'top_a': 0,
        'repetition_penalty': 1.18,
        'repetition_penalty_range': 0,
        'top_k': 40,
        'min_length': 0,
        'no_repeat_ngram_size': 0,
        'num_beams': 1,
        'penalty_alpha': 0,
        'length_penalty': 1,
        'early_stopping': false,
        'mirostat_mode': 0,
        'mirostat_tau': 5,
        'mirostat_eta': 0.1,
        'guidance_scale': 1,
        'negative_prompt': '',

        'seed': -1,
        'add_bos_token': true,
        'truncation_length': 2048,
        'ban_eos_token': false,
        'custom_token_bans': '',
        'skip_special_tokens': true,
        'stopping_strings': []
      })
    }
)
};
 */
}
