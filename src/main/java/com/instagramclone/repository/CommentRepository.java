package com.instagramclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instagramclone.entity.Comment;
import com.instagramclone.entity.Post;
import com.instagramclone.entity.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
	
	List<Comment> findByPostId(long postId);
	
	List<Comment> findByPost(Post post);       
    List<Comment> findByUser(User user); 

}
