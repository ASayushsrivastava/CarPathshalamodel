package com.carPathshala.mapper;

import com.carPathshala.dto.blog.BlogRequest;
import com.carPathshala.dto.blog.BlogResponse;
import com.carPathshala.model.Blog;

public final class BlogMapper {
	private BlogMapper() {}
	public static Blog toEntity(BlogRequest req) {
        return Blog.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .author(req.getAuthor())
                .build();
    }

    public static void updateEntity(Blog entity, BlogRequest req) {
        entity.setTitle(req.getTitle());
        entity.setContent(req.getContent());
        entity.setAuthor(req.getAuthor());
        // updatedAt handled by @PreUpdate
    }

    public static BlogResponse toResponse(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .author(blog.getAuthor())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }
}
