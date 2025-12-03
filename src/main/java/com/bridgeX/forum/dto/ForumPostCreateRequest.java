package com.bridgeX.forum.dto;

import com.bridgeX.forum.domain.ContentTag;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumPostCreateRequest {
    private String title;
    private String content;
    private ContentTag tag;
    private String author;
}
