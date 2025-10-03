package com.carPathshala.dto.ai;

import lombok.Data;

@Data
public class AiSuggestResponse {
	private String suggestion;
    private String model;
    private long latencyMs;
}
