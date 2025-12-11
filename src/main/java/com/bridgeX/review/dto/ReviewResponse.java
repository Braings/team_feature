package com.bridgeX.review.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponse {
	private long id; // not user id!!! just Review's id (identifier)
    private String title;    
    private String content;    
    private String tag;    
    
    private String nickname; // Real Display
    
    private LocalDateTime creationTime;
    
    private Integer suggestion;
    private Integer views;
    
    private boolean suggestedByCurrentUser; // 좋아요 눌렀나 안했나 확인용
}
