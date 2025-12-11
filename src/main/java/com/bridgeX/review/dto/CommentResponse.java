package com.bridgeX.review.dto;

import java.time.LocalDateTime;

import com.bridgeX.review.domain.ReviewComment;
import lombok.Getter;

@Getter
public class CommentResponse {

    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Applied Setter
    public CommentResponse(ReviewComment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getUser().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}

