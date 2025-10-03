package com.carPathshala.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AIProps {
	private String provider = "openai";      
    private String model = "gpt-4o-mini";    
    private Double temperature = 0.7;
    private int maxTokens = 600;
    private int timeoutMs = 20000;
    private String baseUrl = "https://api.openai.com";
    private String apiKey;  // Loaded securely from .env or app properties

    private int maxInputChars = 6000;        

    private RateLimit rateLimit = new RateLimit();

    @Data
    public static class RateLimit {
        private int capacity = 60;
        private int refillTokens = 60;
        private int refillDurationSeconds = 3600;
    }
}
