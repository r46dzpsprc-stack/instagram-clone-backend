package com.instagramclone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instagramclone.entity.Like;
import com.instagramclone.entity.Post;
import com.instagramclone.entity.User;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
	
	Optional<Like> findByUserAndPost(User user, Post post);
	
	long countByPost(Post post);
	
	List<Like> findByPost(Post post);

}
