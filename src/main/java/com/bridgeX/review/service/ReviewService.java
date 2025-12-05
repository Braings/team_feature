package com.bridgeX.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgeX.review.domain.ReviewInfo;
import com.bridgeX.review.dto.ReviewCreateRequest;
import com.bridgeX.review.dto.ReviewUpdateRequest;
import com.bridgeX.review.repository.ReviewPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewPostRepository repository;

    public ReviewInfo createPost(ReviewCreateRequest request) {
        ReviewInfo post = ReviewInfo.builder()
        		.title(request.getTitle())
        		.content(request.getContent())
        		.tag(request.getTag())
        		.username(request.getUsername())
        		.suggestion(request.getSuggestion())
        		.views(request.getViews())
        		.build();
        
        return repository.save(post);
    }
    
    public void updatePost(Long id, ReviewUpdateRequest request, String currentUser) {
        ReviewInfo post = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("[SERVER] 리뷰를 찾을 수 없습니다."));

        if (!post.getUsername().equals(currentUser)) {
            throw new RuntimeException("[SERVER] 게시글을 수정할 권한이 없습니다.");
        }

        post.update(request);
        repository.save(post);
    }


    public List<ReviewInfo> getAllPosts() {
        return repository.findAll();
    }

    public ReviewInfo getPost(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("[SERVER] 리뷰를 찾을 수 없습니다."));
    }

    public void deletePost(Long id) {
        repository.deleteById(id);
    }
}
