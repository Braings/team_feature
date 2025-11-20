package com.bridgeX.forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeX.forum.domain.ForumPost;
import com.bridgeX.forum.dto.ForumPostCreateRequest;
import com.bridgeX.forum.service.ForumPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forum")
public class ForumPostController {

    private final ForumPostService service;

    @PostMapping("/create")
    public ForumPost create(@RequestBody ForumPostCreateRequest request) {
    	return service.createPost(request);
    }

    @GetMapping
    public List<ForumPost> list() {
        return service.getAllPosts();
    }

    @GetMapping("/{id}")
    public ForumPost get(@PathVariable Long id) {
        return service.getPost(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePost(id);
    }
}
