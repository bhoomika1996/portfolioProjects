package com.bg.codeReviewer.controller;

import com.bg.codeReviewer.dto.CodeReviewRequest;
import com.bg.codeReviewer.dto.CodeReviewResponse;
import com.bg.codeReviewer.service.CodeReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/code-review")
public class CodeReviewController {

    private final CodeReviewService codeReviewService;

    public CodeReviewController(CodeReviewService codeReviewService) {
        this.codeReviewService = codeReviewService;
    }

    @PostMapping
    public ResponseEntity<CodeReviewResponse> reviewCode(@RequestBody CodeReviewRequest request) throws JsonProcessingException {
        log.info("Request body: "+request.toString());
        CodeReviewResponse response = codeReviewService.reviewCode(request);
        return ResponseEntity.ok(response);
    }
}

