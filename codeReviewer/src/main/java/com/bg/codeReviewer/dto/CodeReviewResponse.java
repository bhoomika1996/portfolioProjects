package com.bg.codeReviewer.dto;

import lombok.Data;

import java.util.List;

@Data
public class CodeReviewResponse {
    private List<String> issues;
    private List<String> suggestions;
    private String difficulty;

    // Getters and setters
}
