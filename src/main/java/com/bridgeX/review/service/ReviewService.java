package com.bridgeX.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgeX.review.domain.ReviewInfo;
import com.bridgeX.review.dto.ReviewCreateRequest;
import com.bridgeX.review.repository.ReviewPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewPostRepository repository;

    public ReviewInfo createPost(ReviewCreateRequest request) {
        ReviewInfo post = new ReviewInfo();
        
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setTag(request.getTag());
        post.setUsername(request.getUsername());
        post.setSuggestion(request.getSuggestion());
        post.setViews(request.getViews());
        
        return repository.save(post);
    }

    public List<ReviewInfo> getAllPosts() {
        return repository.findAll();
    }

    public ReviewInfo getPost(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("[SERVER]: Post not found"));
    }

    public void deletePost(Long id) {
        repository.deleteById(id);
    }
}
