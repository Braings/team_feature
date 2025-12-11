package com.bridgeX.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeX.review.dto.CommentCreateRequest;
import com.bridgeX.review.dto.CommentResponse;
import com.bridgeX.review.dto.CommentUpdateRequest;
import com.bridgeX.review.dto.ReviewCreateRequest;
import com.bridgeX.review.dto.ReviewListResponse;
import com.bridgeX.review.dto.ReviewResponse;
import com.bridgeX.review.dto.ReviewUpdateRequest;
import com.bridgeX.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService service;

    /* ---------------------------------------- Review ---------------------------------------- */
    
    @PostMapping("/reviews")
    public ResponseEntity<Void> create(@RequestBody ReviewCreateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        service.createPost(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201
    }


    @PutMapping("/reviews/{id}")
    public void edit(@PathVariable("id") Long id, @RequestBody ReviewUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        service.updatePost(id, request, userDetails.getUsername());
    }

    
    @GetMapping("/reviews")
    public List<ReviewListResponse> list() {
        return service.getAllPosts();
    }

    
    @GetMapping("/reviews/{id}")
    public ReviewResponse get(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
    	String currentUserUsername = (userDetails != null) ? userDetails.getUsername() : null;
        return service.getPost(id, currentUserUsername);
    }

    
    @DeleteMapping("/reviews/{id}")
    public void delete(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {

        String currentUser = userDetails.getUsername();
        service.deletePost(id, currentUser);
    }
    
    
    @PostMapping("/reviews/{id}/recommend")
    public ResponseEntity<Void> suggest(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        service.suggestReview(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
    
    /* ---------------------------------------- Comment ---------------------------------------- */
    
    // Comment List 조회하기
    @GetMapping("/reviews/{id}/comments")
    public List<CommentResponse> getComments(@PathVariable("id") Long id) {
        return service.getComments(id);
    }
    
    // Add Comment
    @PostMapping("/reviews/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable("id") Long id, @RequestBody CommentCreateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        service.addComment(id, userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Edit Comment
    @PutMapping("/reviews/{id}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id, @PathVariable Long commentId, @RequestBody CommentUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        service.updateComment(id, commentId, userDetails.getUsername(), request);
        return ResponseEntity.ok().build();
    }


    // Delete Comment
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        service.deleteComment(commentId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

}
