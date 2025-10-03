package com.carPathshala.dto.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiSuggestRequest {
    @NotBlank
    private String task;        // TITLE | OUTLINE | BODY | REWRITE | TAGS
    private String inputText;   // brief / selected text / section
    private String context;     // optional: blog title or more context
    private String tone;        // optional: "professional", "casual", etc
    private Integer length;     // desired words (for BODY)
}
