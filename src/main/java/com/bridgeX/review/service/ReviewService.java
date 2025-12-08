package com.bridgeX.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bridgeX.review.domain.ReviewInfo;
import com.bridgeX.review.domain.ReviewSuggestion;
import com.bridgeX.review.dto.ReviewCreateRequest;
import com.bridgeX.review.dto.ReviewListResponse;
import com.bridgeX.review.dto.ReviewResponse;
import com.bridgeX.review.dto.ReviewUpdateRequest;
import com.bridgeX.review.repository.ReviewPostRepository;
import com.bridgeX.review.repository.ReviewSuggestionRepository;
import com.bridgeX.user.domain.SiteUser;
import com.bridgeX.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewPostRepository repository;
    private final UserRepository userRepository;
    private final ReviewSuggestionRepository reviewSuggestionRepository;
    
    
    // Post Review
    public void createPost(ReviewCreateRequest request, String currentUser) {
        SiteUser user = userRepository.findByUsername(currentUser)
                .orElseThrow(() -> new RuntimeException("[SERVER] 유저를 찾을 수 없습니다."));

        ReviewInfo post = ReviewInfo.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .tag(request.getTag())
                .suggestion(request.getSuggestion())
                .views(0)
                .build();

        repository.save(post);
    }
    
    
    // Edit Review
    public void updatePost(Long id, ReviewUpdateRequest request, String currentUser) {
    	ReviewInfo post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("[SERVER] 리뷰를 찾을 수 없습니다."));

        if (!post.getUser().getUsername().equals(currentUser)) {
            throw new RuntimeException("[SERVER] 게시글을 수정할 권한이 없습니다.");
        }

        post.update(request);
        repository.save(post);
    }

    
    // Review List
    public List<ReviewListResponse> getAllPosts() {
        return repository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    private ReviewListResponse convertToDto(ReviewInfo entity) {
    	ReviewListResponse dto = ReviewListResponse.builder()
    			.id(entity.getId())
    			.title(entity.getTitle())
    			.username(entity.getUser().getUsername())
    			.nickname(entity.getUser().getNickname())
    			.tag(entity.getTag().name())
    			.creationTime(entity.getDate())
    			.suggestion(entity.getSuggestion())
    			.views(entity.getViews())
    			.build();
    	
        return dto;
    }
    
    
    // Review
    public ReviewResponse getPost(Long id) {
        ReviewInfo post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("[SERVER] 리뷰를 찾을 수 없습니다."));
        
        // 조회수 +1
        post.increaseViews();
        repository.save(post);
        
        return convertToDetailDto(post);
    }
    private ReviewResponse convertToDetailDto(ReviewInfo entity) {
        return ReviewResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .username(entity.getUser().getUsername())
                .nickname(entity.getUser().getNickname())
                .tag(entity.getTag().name())
                .creationTime(entity.getDate())
                .suggestion(entity.getSuggestion())
                .views(entity.getViews())
                .build();
    }


    
    // Delete Review
    public void deletePost(Long id, String currentUser) {
        ReviewInfo post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("[SERVER] 리뷰를 찾을 수 없습니다."));

        // 작성자 확인
        String author = post.getUser().getUsername();

        if (!author.equals(currentUser)) {
            throw new RuntimeException("[SERVER] 게시글 삭제 권한이 없습니다.");
        }

        repository.delete(post);
    }

    
    // Suggest Review
    @Transactional
    public boolean suggestReview(Long reviewId, String currentUsername) {

        ReviewInfo review = repository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("[SERVER] 리뷰를 찾을 수 없습니다."));

        SiteUser user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("[SERVER] 유저를 찾을 수 없습니다."));

        // 이미 추천했는지 확인
        Optional<ReviewSuggestion> existing = reviewSuggestionRepository.findByReviewAndUser(review, user);

        if (existing.isPresent()) {
            // 이미 추천했으면 추천 취소
            reviewSuggestionRepository.delete(existing.get());
            review.decreaseSuggestion();
            return false;
        } else {
            // 아직 안 했으면 추천
            ReviewSuggestion suggestion = ReviewSuggestion.builder()
            		.review(review)
            		.user(user)
            		.build();
            
            reviewSuggestionRepository.save(suggestion);

            review.increaseSuggestion();
            return true;
        }
    }

}
