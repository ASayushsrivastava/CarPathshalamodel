package com.carPathshala.ai;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

@Service
public class PromptService {

    public String loadPrompt(String task, String version, Map<String,String> vars) throws Exception {
        String path = String.format("prompts/%s/%s.txt", task.toLowerCase(), version);
        ClassPathResource res = new ClassPathResource(path);
        if (!res.exists()) {
            throw new IllegalArgumentException("Prompt not found: " + path);
        }
        try (InputStream is = res.getInputStream(); Scanner sc = new Scanner(is, StandardCharsets.UTF_8)) {
            sc.useDelimiter("\\A");
            String template = sc.hasNext() ? sc.next() : "";
            // Simple placeholder replacement: {{key}} -> value
            if (vars != null) {
                for (Map.Entry<String,String> e : vars.entrySet()) {
                    template = template.replace("{{" + e.getKey() + "}}", e.getValue() == null ? "" : e.getValue());
                }
            }
            return template;
        }
    }
}
