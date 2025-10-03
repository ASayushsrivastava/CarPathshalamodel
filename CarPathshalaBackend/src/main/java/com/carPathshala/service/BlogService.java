package com.carPathshala.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.carPathshala.dto.blog.BlogRequest;
import com.carPathshala.dto.blog.BlogResponse;

public interface BlogService {
	BlogResponse create(BlogRequest request);
    BlogResponse update(Long id, BlogRequest request);
    void delete(Long id);
    BlogResponse getById(Long id);
    Page<BlogResponse> list(Pageable pageable);
    Page<BlogResponse> searchByTitle(String keyword, Pageable pageable);
    Page<BlogResponse> listByAuthor(String author, Pageable pageable);
}
