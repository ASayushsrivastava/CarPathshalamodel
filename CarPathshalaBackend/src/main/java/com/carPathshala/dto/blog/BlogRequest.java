package com.carPathshala.dto.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogRequest {
    @NotBlank @Size(min = 3, max = 150)
    private String title;

    @NotBlank @Size(min = 10)
    private String content;

    @NotBlank @Size(min = 2, max = 60)
    private String author;
}