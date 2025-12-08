package com.bridgeX.review.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

// for List. Not include 'contents'

@Getter
@Builder
public class ReviewListResponse {
	private long id; // not user id!!! just Review's id (identifier)
    private String title;        
    private String tag;    
    private String username; // Author identifier. Not displayed
    private String nickname; // Real Display
    
    private LocalDateTime creationTime;
    
    private Integer suggestion;
    private Integer views;
}
