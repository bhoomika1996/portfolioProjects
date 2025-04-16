package com.bg.codeReviewer.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class CodellamaResponse {
    private String model;
    private ZonedDateTime created_at;
    private String response;
    private boolean done;
    private String done_reason;
    private List<Integer> context;
    private long total_duration;
    private long load_duration;
    private int prompt_eval_count;
    private long prompt_eval_duration;
    private int eval_count;
    private long eval_duration;
}
