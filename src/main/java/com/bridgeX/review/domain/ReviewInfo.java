package com.bridgeX.review.domain;

import java.time.LocalDateTime;

import com.bridgeX.review.dto.ReviewUpdateRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;


import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    private ContentTag tag;
 
    private String username;

    // DTO: creationTime
    private LocalDateTime date;
    
    private Integer suggestion;
    
    private Integer views;

    @PrePersist
    public void onCreate() {
        this.date = LocalDateTime.now();
    }

    public void update(ReviewUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.tag = request.getTag();
    }

}
