package com.bg.codeReviewer;

import com.bg.codeReviewer.dto.CodeReviewRequest;
import com.bg.codeReviewer.dto.CodeReviewResponse;
import com.bg.codeReviewer.dto.CodellamaResponse;
import com.bg.codeReviewer.service.CodeReviewService;
import com.bg.codeReviewer.service.InferenceClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CodeReviewServiceTest {

    @Autowired
    private CodeReviewService service;

    @MockBean
    private InferenceClient inferenceClient;

    @Test
    public void testReviewCode() throws JsonProcessingException {
        CodeReviewRequest request = new CodeReviewRequest("java", "public class Test {}");

        CodeReviewResponse mockResponse = new CodeReviewResponse();
        mockResponse.setIssues(List.of("Test Issue"));
        mockResponse.setSuggestions(List.of("Test Suggestion"));
        mockResponse.setDifficulty("Easy");

        CodellamaResponse codellamaResponse = new CodellamaResponse();
        codellamaResponse.setResponse("Test Issue");

        Mockito.when(inferenceClient.analyzeCode(Mockito.any()))
                .thenReturn(codellamaResponse);

        CodeReviewResponse result = service.reviewCode(request);

        assertEquals("Easy", result.getDifficulty());
        assertTrue(result.getIssues().contains("Test Issue"));
    }
}

