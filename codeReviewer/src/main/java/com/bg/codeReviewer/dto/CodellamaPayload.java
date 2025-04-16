package com.bg.codeReviewer.dto;

import lombok.Data;

@Data
public class CodellamaPayload {
    private String model;
    private String prompt;
    private boolean stream;
}
