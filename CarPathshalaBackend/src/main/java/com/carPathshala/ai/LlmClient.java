package com.carPathshala.ai;

import java.util.Map;

public interface LlmClient {
	LlmResult generate(String systemPrompt, String userPrompt, Map<String, Object> params) throws Exception;

    class LlmResult {
        public final String text;
        public final String model;
        public final long latencyMs;

        public LlmResult(String text, String model, long latencyMs) {
            this.text = text;
            this.model = model;
            this.latencyMs = latencyMs;
        }
    }
}
