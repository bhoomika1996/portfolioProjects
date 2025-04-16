package com.bg.codeReviewer.service;

import com.bg.codeReviewer.dto.CodeReviewRequest;
import com.bg.codeReviewer.dto.CodeReviewResponse;
import com.bg.codeReviewer.dto.CodellamaPayload;
import com.bg.codeReviewer.dto.CodellamaResponse;
import com.bg.codeReviewer.model.CodeSnippet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class WrapperService {

    public CodeReviewResponse wrapCodellamaResToCodeReviewRes(CodellamaResponse response) throws JsonProcessingException {
        String escapedJson = response.getResponse();
        escapedJson = escapedJson.trim();
        ObjectMapper mapper = new ObjectMapper();
        CodeReviewResponse feedback = mapper.readValue(escapedJson, CodeReviewResponse.class);
        return feedback;
    }

    public CodellamaPayload wrapCodeReviewReqToCodellamaPayload(CodeReviewRequest request){
        CodellamaPayload payload = new CodellamaPayload();
        payload.setModel("codellama");
        payload.setStream(false);
        String prompt = "You are a JSON-only code review assistant. You will be given a code snippet in a specific programming language. You must identify:\n\n1. A list of issues in the code\n2. Suggestions to fix them\n3. An estimated difficulty level of understanding the snippet\n\nYou MUST reply with only a valid JSON object, and NOTHING else — no explanations, no preamble, no markdown, no commentary.\n\n⚠️ If you return anything except valid JSON matching the exact schema below, your response will fail automatic validation.\n\nExpected JSON format:\n{\n  \"issues\": [\"string\", \"string\"],\n  \"suggestions\": [\"string\", \"string\"],\n  \"difficulty\": \"Easy\" // or \"Medium\" or \"Hard\"\n}\n\nAll fields are required. Do not leave anything out. Do not explain anything after the JSON.\n\nNow analyze this "+request.getLanguage()+" code:\n\n"+request.getCode()+"\n\nRespond with valid JSON only.";

        payload.setPrompt(prompt);
        return payload;
    }

//    public CodeSnippet wrapCodeReviewResponseToCodeSnippet(CodellamaResponse response){
//        return null;
//    }
}
