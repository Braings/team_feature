package com.bridgeX.forum.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumPostCreateRequest {
    private String title;
    private String content;
    private String author;
}
