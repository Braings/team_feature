package com.bridgeX.review.domain;

import java.time.LocalDateTime;

import com.bridgeX.user.domain.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ReviewComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // not Review's ID!! Just Comment's ID

    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewInfo review;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser user;

    @Column(nullable = false, length = 1000)
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Applied Setter
    public static ReviewComment create(ReviewInfo review, SiteUser user, String content) {
        ReviewComment c = new ReviewComment();
        c.review = review;
        c.user = user;
        c.content = content;
        c.createdAt = LocalDateTime.now();
        c.updatedAt = LocalDateTime.now();
        return c;
    }

    // Domain Logic
    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    // Applied Getter
    public Long getId() { return id; }
    public ReviewInfo getReview() { return review; }
    public SiteUser getUser() { return user; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

