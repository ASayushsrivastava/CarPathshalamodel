package com.carPathshala.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carPathshala.dto.newsLetter.NewsletterSubscribeRequest;
import com.carPathshala.dto.newsLetter.NewsletterSubscriberResponse;
import com.carPathshala.dto.newsLetter.NewsletterUnsubscribeRequest;
import com.carPathshala.service.NewsletterService;

@RestController
@RequestMapping("/api/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;

    /* Public: subscribe */
    @PostMapping("/subscribe")
    public ResponseEntity<NewsletterSubscriberResponse> subscribe(
            @Valid @RequestBody NewsletterSubscribeRequest request) {
        return ResponseEntity.ok(newsletterService.subscribe(request));
    }

    /* Public: unsubscribe */
    @PostMapping("/unsubscribe")
    public ResponseEntity<NewsletterSubscriberResponse> unsubscribe(
            @Valid @RequestBody NewsletterUnsubscribeRequest request) {
        return ResponseEntity.ok(newsletterService.unsubscribe(request));
    }

    /* Admin (protect later with JWT / roles): list all */
    @GetMapping("/subscribers")
    public ResponseEntity<Page<NewsletterSubscriberResponse>> listAll(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(newsletterService.listAll(pageable));
    }

    /* Admin (protect later): list ACTIVE only */
    @GetMapping("/subscribers/active")
    public ResponseEntity<Page<NewsletterSubscriberResponse>> listActive(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(newsletterService.listActive(pageable));
    }

    /* Admin (protect later): delete by id */
    @DeleteMapping("/subscribers/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        newsletterService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

