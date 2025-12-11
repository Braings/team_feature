package com.bridgeX.review.dto;

import com.bridgeX.review.domain.ContentTag;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequest {
	private String title;
    private String content;
    private ContentTag tag;
}
