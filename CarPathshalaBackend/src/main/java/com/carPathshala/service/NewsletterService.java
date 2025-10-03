package com.carPathshala.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.carPathshala.dto.newsLetter.NewsletterSubscribeRequest;
import com.carPathshala.dto.newsLetter.NewsletterSubscriberResponse;
import com.carPathshala.dto.newsLetter.NewsletterUnsubscribeRequest;

public interface NewsletterService {
    NewsletterSubscriberResponse subscribe(NewsletterSubscribeRequest request);
    NewsletterSubscriberResponse unsubscribe(NewsletterUnsubscribeRequest request);
    Page<NewsletterSubscriberResponse> listAll(Pageable pageable);
    Page<NewsletterSubscriberResponse> listActive(Pageable pageable);
    void deleteById(Long id);
}
