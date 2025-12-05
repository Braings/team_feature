package com.bridgeX.review.dto;

import com.bridgeX.review.domain.ContentTag;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewUpdateRequest {
	private String title;
    private String content;
    private ContentTag tag;
}
