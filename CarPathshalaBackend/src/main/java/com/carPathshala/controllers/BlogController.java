package com.carPathshala.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.carPathshala.dto.blog.BlogRequest;
import com.carPathshala.dto.blog.BlogResponse;
import com.carPathshala.service.BlogService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "http://localhost:5173")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<BlogResponse> create(@Valid @RequestBody BlogRequest request,
                                               UriComponentsBuilder uriBuilder) {
        BlogResponse created = blogService.create(request);
        return ResponseEntity.created(
                uriBuilder.path("/api/blogs/{id}").buildAndExpand(created.getId()).toUri()
        ).body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BlogResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody BlogRequest request) {
        return ResponseEntity.ok(blogService.update(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getById(id));
    }

    // LIST (paginated)
    @GetMapping
    public ResponseEntity<Page<BlogResponse>> list(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(blogService.list(pageable));
    }

    // SEARCH (paginated)
    @GetMapping("/search")
    public ResponseEntity<Page<BlogResponse>> search(
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(blogService.searchByTitle(keyword, pageable));
    }

    // FILTER BY AUTHOR (paginated)
    @GetMapping("/author/{author}")
    public ResponseEntity<Page<BlogResponse>> listByAuthor(
            @PathVariable String author,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(blogService.listByAuthor(author, pageable));
    }
}