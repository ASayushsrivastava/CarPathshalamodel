package com.carPathshala.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
    name = "newsletter_subscribers",
    uniqueConstraints = @UniqueConstraint(name = "uk_newsletter_email", columnNames = "email"),
    indexes = {
        @Index(name = "idx_newsletter_email", columnList = "email"),
        @Index(name = "idx_newsletter_status", columnList = "status")
    }
)
public class NewsletterSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)                // store lowercase; enforce in service
    private String email;

    @Column(nullable = false)
    private String status;                   // ACTIVE | UNSUBSCRIBED

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // (Optional) audit fields for IP / source can be added later
}
