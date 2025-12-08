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

import com.bridgeX.review.dto.ReviewCreateRequest;
import com.bridgeX.review.dto.ReviewListResponse;
import com.bridgeX.review.dto.ReviewResponse;
import com.bridgeX.review.dto.ReviewUpdateRequest;
import com.bridgeX.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService service;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ReviewCreateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        service.createPost(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201
    }


    @PutMapping("/edit/{id}")
    public void edit(@PathVariable Long id, @RequestBody ReviewUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        service.updatePost(id, request, userDetails.getUsername());
    }

    
    @GetMapping
    public List<ReviewListResponse> list() {
        return service.getAllPosts();
    }

    @GetMapping("/{id}")
    public ReviewResponse get(@PathVariable Long id) {
        return service.getPost(id);
    }

    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        String currentUser = userDetails.getUsername();
        service.deletePost(id, currentUser);
    }
}
