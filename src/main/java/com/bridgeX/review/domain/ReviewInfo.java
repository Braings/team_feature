package com.bridgeX.review.domain;

import java.time.LocalDateTime;

import com.bridgeX.review.dto.ReviewUpdateRequest;
import com.bridgeX.user.domain.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SiteUser user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private ContentTag tag;

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

    // views +1
    public void increaseViews() {
        if(this.views == null) { this.views = 0; } // 예외 처리
        this.views += 1; // else
    }
    
    // suggestion +1
    public void increaseSuggestion() {
        if(this.suggestion == null) { this.suggestion = 0; } // 예외 처리
        this.suggestion += 1;
    }

    // suggestion -1
    public void decreaseSuggestion() {
        if(this.suggestion == null) this.suggestion = 0; // 예외 처리
        if(this.suggestion > 0) this.suggestion -= 1;
    }
}
