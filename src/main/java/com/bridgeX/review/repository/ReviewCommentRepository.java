package com.bridgeX.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgeX.review.domain.ReviewComment;
import com.bridgeX.review.domain.ReviewInfo;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findByReviewOrderByCreatedAtAsc(ReviewInfo review);
}

