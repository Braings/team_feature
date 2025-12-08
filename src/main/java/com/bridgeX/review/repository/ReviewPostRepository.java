package com.bridgeX.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgeX.review.domain.ReviewInfo;

public interface ReviewPostRepository extends JpaRepository<ReviewInfo, Long> {
	
}
