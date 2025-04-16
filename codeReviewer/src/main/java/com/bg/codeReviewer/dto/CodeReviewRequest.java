package com.bg.codeReviewer.dto;

import lombok.Data;

@Data
public class CodeReviewRequest {
    private String language;
    private String code;

    public CodeReviewRequest(String java, String s) {
    }

    // Getters and setters
}

