package com.bridgeX.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bridgeX.forum.domain.ForumPost;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
	
}
