package com.bridgeX.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeX.review.domain.ReviewComment;
import com.bridgeX.review.domain.ReviewInfo;
import com.bridgeX.review.domain.ReviewSuggestion;

import com.bridgeX.review.dto.CommentCreateRequest;
import com.bridgeX.review.dto.CommentResponse;
import com.bridgeX.review.dto.CommentUpdateRequest;
import com.bridgeX.review.dto.ReviewCreateRequest;
import com.bridgeX.review.dto.ReviewListResponse;
import com.bridgeX.review.dto.ReviewResponse;
import com.bridgeX.review.dto.ReviewUpdateRequest;

import com.bridgeX.review.repository.ReviewCommentRepository;
import com.bridgeX.review.repository.ReviewPostRepository;
import com.bridgeX.review.repository.ReviewSuggestionRepository;

import com.bridgeX.user.domain.SiteUser;

import com.bridgeX.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewPostRepository repository;
    private final UserRepository userRepository;
    private final ReviewSuggestionRepository reviewSuggestionRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    
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
    			.nickname(entity.getUser().getNickname())
    			.tag(entity.getTag().name())
    			.creationTime(entity.getDate())
    			.suggestion(entity.getSuggestion())
    			.views(entity.getViews())
    			.build();
    	
        return dto;
    }
    
    
    // Review
    public ReviewResponse getPost(Long id, String currentUserUsername) {
        ReviewInfo post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("[SERVER] 리뷰를 찾을 수 없습니다."));
        
        // 조회수 +1
        post.increaseViews();
        repository.save(post);
        
        return convertToDetailDto(post, currentUserUsername);
    }
    private ReviewResponse convertToDetailDto(ReviewInfo entity, String currentUserUsername) {
    	SiteUser user = userRepository.findByUsername(currentUserUsername).orElse(null);
    	boolean isSuggested = false;
    	// 유저 엔티티가 존재할 경우에만 추천 여부를 확인
    	if (user != null) {
    		isSuggested = reviewSuggestionRepository.existsByReviewAndUser(entity, user);
    	}
    	
        try {
            // 원본 로직 유지: DTO를 생성하고 반환
            return ReviewResponse.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .nickname(entity.getUser() != null ? entity.getUser().getNickname() : "Unknown") 
                    .tag(entity.getTag().name()) 
                    .creationTime(entity.getDate())
                    .suggestion(entity.getSuggestion())
                    .views(entity.getViews())
                    .suggestedByCurrentUser(isSuggested)
                    .build();
        } catch (Exception e) {
            // 예외 발생 시 서버 콘솔에 상세 정보 출력
            System.err.println("--- FATAL DTO CONVERSION ERROR: Review ID " + entity.getId() + " ---");
            e.printStackTrace();
            
            // DTO 변환 실패는 서버 측의 잘못된 데이터 또는 로직 오류이므로, RuntimeException을 던져서 API 호출자가 400 또는 500 응답을 처리
            throw new RuntimeException("API 문제: DTO 변환 오류", e); 
        }
    }
    /*
    private ReviewResponse convertToDetailDto(ReviewInfo entity) {
        return ReviewResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .nickname(entity.getUser().getNickname())
                .tag(entity.getTag().name())
                .creationTime(entity.getDate())
                .suggestion(entity.getSuggestion())
                .views(entity.getViews())
                .build();
    }
    */


    
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

    // 댓글 작성
    @Transactional
    public void addComment(Long reviewId, String currentUsername, CommentCreateRequest request) {

        ReviewInfo review = repository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("[SERVER] 리뷰를 찾을 수 없습니다."));

        SiteUser user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("[SERVER] 유저를 찾을 수 없습니다."));

        ReviewComment comment = ReviewComment.create(review, user, request.getContent());
        reviewCommentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long reviewId, Long commentId, String currentUsername,
                              CommentUpdateRequest request) {

        ReviewComment comment = reviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("[SERVER] 댓글을 찾을 수 없습니다."));

        // 예외 처리
        if (!comment.getReview().getId().equals(reviewId)) {
            throw new RuntimeException("[SERVER] 이 리뷰의 댓글이 아닙니다.");
        }

        // 작성자 체크
        if (!comment.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("[SERVER] 댓글을 수정할 권한이 없습니다.");
        }

        comment.updateContent(request.getContent());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, String currentUsername) {

        ReviewComment comment = reviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("[SERVER] 댓글을 찾을 수 없습니다."));
        // 예외 처리
        if (!comment.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("[SERVER] 댓글을 삭제할 권한이 없습니다.");
        }

        reviewCommentRepository.delete(comment);
    }

    // 댓글 목록 요청
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long reviewId) {

        ReviewInfo review = repository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("[SERVER] 리뷰를 찾을 수 없습니다."));

        return reviewCommentRepository.findByReviewOrderByCreatedAtAsc(review)
                .stream()
                .map(CommentResponse::new)
                .toList();
    }
}
