package com.bridgeX.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgeX.review.domain.ReviewInfo;
import com.bridgeX.review.domain.ReviewSuggestion;
import com.bridgeX.user.domain.SiteUser;

public interface ReviewSuggestionRepository extends JpaRepository<ReviewSuggestion, Long> {

    boolean existsByReviewAndUser(ReviewInfo review, SiteUser user);

    long countByReview(ReviewInfo review);
    
    Optional<ReviewSuggestion> findByReviewAndUser(ReviewInfo review, SiteUser user);
}

