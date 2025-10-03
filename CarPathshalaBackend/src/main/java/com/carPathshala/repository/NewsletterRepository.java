package com.carPathshala.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carPathshala.model.NewsletterSubscriber;

import java.util.Optional;
@Repository
public interface NewsletterRepository extends JpaRepository<NewsletterSubscriber, Long> {
    boolean existsByEmail(String email);
    Optional<NewsletterSubscriber> findByEmail(String email);
    Page<NewsletterSubscriber> findAllByStatus(String status, Pageable pageable);
}

