package com.bridgeX.review.domain;

import com.bridgeX.user.domain.SiteUser;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(
    name = "review_suggestion",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"review_id", "user_id"})
    }
)
@Getter
@Builder
public class ReviewSuggestion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewInfo review;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser user;
}

