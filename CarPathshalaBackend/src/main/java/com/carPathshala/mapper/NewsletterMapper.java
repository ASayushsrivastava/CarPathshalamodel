package com.carPathshala.mapper;

import com.carPathshala.dto.newsLetter.NewsletterSubscribeRequest;
import com.carPathshala.dto.newsLetter.NewsletterSubscriberResponse;
import com.carPathshala.model.NewsletterSubscriber;

public class NewsletterMapper {

    public static NewsletterSubscriber toEntity(NewsletterSubscribeRequest req) {
        return NewsletterSubscriber.builder()
                .email(req.getEmail().toLowerCase().trim())
                .status("ACTIVE")
                .build();
    }

    public static NewsletterSubscriberResponse toResponse(NewsletterSubscriber s) {
        return NewsletterSubscriberResponse.builder()
                .id(s.getId())
                .email(s.getEmail())
                .status(s.getStatus())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
