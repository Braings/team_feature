package com.bridgeX.review.dto;

import java.time.LocalDateTime;

import com.bridgeX.review.domain.ContentTag;

public class ReviewResponse {
	private long id; // not user id!!! just Review's id (identifier)
    private String title;    
    private String content;    
    private ContentTag tag;    
    private String username; // Author identifier. Not displayed
    private String nickname; // Real Display
    
    private LocalDateTime creationTime;
    
    private Integer suggestion;
    private Integer views;
}
