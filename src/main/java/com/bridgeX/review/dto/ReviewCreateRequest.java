package com.bridgeX.review.dto;

import com.bridgeX.review.domain.ContentTag;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {
    // private LocalDateTime creationTime;
    private String title;
    private String content;
    private ContentTag tag;
    private String username;
    private Integer suggestion;
    private Integer views;
}
