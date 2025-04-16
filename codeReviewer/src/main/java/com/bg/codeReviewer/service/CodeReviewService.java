package com.bg.codeReviewer.service;

import com.bg.codeReviewer.dto.CodeReviewRequest;
import com.bg.codeReviewer.dto.CodeReviewResponse;
import com.bg.codeReviewer.dto.CodellamaPayload;
import com.bg.codeReviewer.dto.CodellamaResponse;
import com.bg.codeReviewer.model.CodeSnippet;
import com.bg.codeReviewer.repository.CodeSnippetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CodeReviewService {

    private final InferenceClient inferenceClient;
    @Autowired
    private WrapperService wrapperService;
    @Autowired
    private CodeSnippetRepository repository;

    public CodeReviewService(InferenceClient inferenceClient) {
        this.inferenceClient = inferenceClient;
    }

    public CodeReviewResponse reviewCode(CodeReviewRequest request) throws JsonProcessingException {
        log.debug("Request body: "+request);
        CodellamaPayload payload = wrapperService.wrapCodeReviewReqToCodellamaPayload(request);
        log.debug("payload body for inferenceClient: "+payload);

        CodellamaResponse codellamaResponse = inferenceClient.analyzeCode(payload);
        log.debug("codellama response: "+codellamaResponse);

        CodeReviewResponse codeReviewResponse = wrapperService.wrapCodellamaResToCodeReviewRes(codellamaResponse);
        log.debug("codeReview response: "+codeReviewResponse);

        CodeSnippet snippet = new CodeSnippet();
        snippet.setCode(request.getCode());
        snippet.setLang(request.getLanguage());
        snippet.setIssues(String.join("\n", codeReviewResponse.getIssues()));
        snippet.setSuggestions(String.join("\n", codeReviewResponse.getSuggestions()));
        snippet.setDifficulty(codeReviewResponse.getDifficulty());
        snippet.setUserId("demoUser"); // Placeholder for real auth
        snippet.setCreatedAt(LocalDateTime.now());
        repository.save(snippet);
        /*
        log.debug("codeSnippet: "+snippet);
        List<CodeSnippet> codeSnippets = listCodeSnippet.findByUserId("demoUser");
        for(CodeSnippet codeSnippet : codeSnippets) {
            log.debug(String.valueOf(codeSnippet));
        }*/
        return codeReviewResponse;
    }
}

