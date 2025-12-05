package com.bridgeX.review.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeX.review.domain.ReviewInfo;
import com.bridgeX.review.dto.ReviewCreateRequest;
import com.bridgeX.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService service;

    @PostMapping("/create")
    public ReviewInfo create(@RequestBody ReviewCreateRequest request) {
    	return service.createPost(request);
    }

    @GetMapping
    public List<ReviewInfo> list() {
        return service.getAllPosts();
    }

    @GetMapping("/{id}")
    public ReviewInfo get(@PathVariable Long id) {
        return service.getPost(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePost(id);
    }
}
