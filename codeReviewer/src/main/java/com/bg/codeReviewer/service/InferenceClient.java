package com.bg.codeReviewer.service;

import com.bg.codeReviewer.dto.CodeReviewRequest;
import com.bg.codeReviewer.dto.CodeReviewResponse;
import com.bg.codeReviewer.dto.CodellamaPayload;
import com.bg.codeReviewer.dto.CodellamaResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class InferenceClient {

    private final WebClient webClient;

    public InferenceClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:11434").build();
    }

    public CodellamaResponse analyzeCode(CodellamaPayload request) {
        return webClient.post()
                .uri("/api/generate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CodellamaResponse.class)
                .block();
    }
}

