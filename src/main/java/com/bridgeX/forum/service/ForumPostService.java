package com.bridgeX.forum.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgeX.forum.domain.ForumPost;
import com.bridgeX.forum.dto.ForumPostCreateRequest;
import com.bridgeX.forum.repository.ForumPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForumPostService {

    private final ForumPostRepository repository;

    public ForumPost createPost(ForumPostCreateRequest request) {
        ForumPost post = new ForumPost();
        
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(request.getAuthor());
        
        return repository.save(post);
    }

    public List<ForumPost> getAllPosts() {
        return repository.findAll();
    }

    public ForumPost getPost(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("[SERVER]: Post not found"));
    }

    public void deletePost(Long id) {
        repository.deleteById(id);
    }
}
