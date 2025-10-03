package com.carPathshala.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carPathshala.dto.newsLetter.NewsletterSubscribeRequest;
import com.carPathshala.dto.newsLetter.NewsletterSubscriberResponse;
import com.carPathshala.dto.newsLetter.NewsletterUnsubscribeRequest;
import com.carPathshala.exceptions.ResourceNotFound;
import com.carPathshala.mapper.NewsletterMapper;
import com.carPathshala.model.NewsletterSubscriber;
import com.carPathshala.repository.NewsletterRepository;
import com.carPathshala.service.NewsletterService;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository newsletterRepository;

    @Override
    public NewsletterSubscriberResponse subscribe(NewsletterSubscribeRequest request) {
        String email = request.getEmail().toLowerCase().trim();

        // If already exists:
        return newsletterRepository.findByEmail(email)
                .map(existing -> {
                    if (!"ACTIVE".equals(existing.getStatus())) {
                        existing.setStatus("ACTIVE");
                        newsletterRepository.save(existing);
                    }
                    return NewsletterMapper.toResponse(existing);
                })
                .orElseGet(() -> {
                    try {
                        NewsletterSubscriber created = newsletterRepository.save(
                                NewsletterMapper.toEntity(request)
                        );
                        return NewsletterMapper.toResponse(created);
                    } catch (DataIntegrityViolationException e) {
                        // Unique constraint edge case race condition
                        NewsletterSubscriber existing = newsletterRepository.findByEmail(email)
                                .orElseThrow(() -> e); // rethrow if truly unexpected
                        if (!"ACTIVE".equals(existing.getStatus())) {
                            existing.setStatus("ACTIVE");
                            newsletterRepository.save(existing);
                        }
                        return NewsletterMapper.toResponse(existing);
                    }
                });
    }

    @Override
    public NewsletterSubscriberResponse unsubscribe(NewsletterUnsubscribeRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        NewsletterSubscriber sub = newsletterRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("Subscriber not found: " + email));

        if (!"UNSUBSCRIBED".equals(sub.getStatus())) {
            sub.setStatus("UNSUBSCRIBED");
            newsletterRepository.save(sub);
        }
        return NewsletterMapper.toResponse(sub);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsletterSubscriberResponse> listAll(Pageable pageable) {
        return newsletterRepository.findAll(pageable).map(NewsletterMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsletterSubscriberResponse> listActive(Pageable pageable) {
        return newsletterRepository.findAllByStatus("ACTIVE", pageable).map(NewsletterMapper::toResponse);
    }

    @Override
    public void deleteById(Long id) {
        if (!newsletterRepository.existsById(id)) {
            throw new ResourceNotFound("Subscriber not found with id: " + id);
        }
        newsletterRepository.deleteById(id);
    }
}

