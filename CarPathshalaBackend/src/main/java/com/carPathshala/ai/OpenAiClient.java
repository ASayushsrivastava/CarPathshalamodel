package com.carPathshala.ai;

import com.carPathshala.ai.LlmClient;
import com.carPathshala.config.AIProps;
import com.carPathshala.exceptions.AiServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiClient implements LlmClient {

    private final AIProps props;

    private WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.getApiKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public LlmResult generate(String systemPrompt, String userPrompt, Map<String, Object> params) {
        long start = System.currentTimeMillis();

        try {
            Map<String, Object> body = Map.of(
                    "model", params.getOrDefault("model", props.getModel()),
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", userPrompt)
                    ),
                    "temperature", params.getOrDefault("temperature", props.getTemperature()),
                    "max_tokens", params.getOrDefault("maxTokens", props.getMaxTokens())
            );

            Map<String, Object> response = createWebClient()
                    .post()
                    .uri("/v1/chat/completions")
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofMillis(props.getTimeoutMs()))
                    .retryWhen(
                            Retry.backoff(2, Duration.ofMillis(500)) // up to 2 retries with exponential backoff
                                    .filter(throwable -> throwable instanceof WebClientResponseException &&
                                            ((WebClientResponseException) throwable).getStatusCode().is5xxServerError())
                    )
                    .block();

            if (response == null) {
                throw new AiServiceException("OpenAI returned null response");
            }

            List<?> choices = (List<?>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new AiServiceException("No AI choices returned");
            }

            Map<?, ?> first = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) first.get("message");
            String text = message.get("content").toString();

            return new LlmResult(text,
                    (String) body.get("model"),
                    System.currentTimeMillis() - start);

        } catch (WebClientResponseException ex) {
            log.error("OpenAI API error: {}", ex.getResponseBodyAsString());
            throw new AiServiceException("OpenAI API responded with error: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error calling OpenAI", ex);
            throw new AiServiceException("Unexpected error calling OpenAI: " + ex.getMessage(), ex);
        }
    }
}
