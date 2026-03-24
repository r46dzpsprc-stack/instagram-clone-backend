package com.instagramclone.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.instagramclone.dto.request.CommentRequestDto;
import com.instagramclone.dto.request.CommentUpdateRequestDto;
import com.instagramclone.dto.response.CommentListResponse;
import com.instagramclone.dto.response.CommentResponseDto;
import com.instagramclone.entity.Comment;
import com.instagramclone.entity.Post;
import com.instagramclone.entity.User;
import com.instagramclone.repository.CommentRepository;
import com.instagramclone.repository.PostRepository;
import com.instagramclone.repository.UserRepository;
import com.instagramclone.service.ICommentService;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new AccessDeniedException("Giriş yapmalısın.");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AccessDeniedException("Kullanıcı bulunamadı."));
    }

    @Override
    public CommentResponseDto addComment(CommentRequestDto dto) {
        User me = getCurrentUser();

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post bulunamadı."));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(me);
        comment.setPost(post);
        comment.setImageUrl(me.getProfileImageUrl());

        commentRepository.save(comment);

        CommentResponseDto resp = new CommentResponseDto();
        resp.setId(comment.getId());
        resp.setContent(comment.getContent());
        resp.setCreatedAt(comment.getCreatedAt());
        resp.setUsername(me.getUsername());
        return resp;
    }

    @Override
    public List<CommentListResponse> getAllComments() {
        User me = getCurrentUser();

        List<Comment> myComments = commentRepository.findByUser(me);
        List<CommentListResponse> out = new ArrayList<>();

        for (Comment c : myComments) {
            CommentListResponse dto = new CommentListResponse();
            dto.setId(c.getId());
            dto.setContent(c.getContent());
            dto.setUsername(c.getUser().getUsername());
            dto.setCreatedAt(c.getCreatedAt());
            out.add(dto);
        }
        return out;
    }

    @Override
    public void deleteComment(long commentId) {
        User me = getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Yorum bulunamadı."));

        if (comment.getUser().getId() != me.getId()) {
            throw new AccessDeniedException("Sadece kendi yorumunu silebilirsin.");
        }
        commentRepository.delete(comment);
    }
    
    @Override
    public List<CommentListResponse> getCommentsByPostId(long postId) {
        // repo’da: List<Comment> findByPostId(long postId);
        List<Comment> list = commentRepository.findByPostId(postId);
        // boş ise exception yerine boş liste döndürmek daha iyi bir pratik
        List<CommentListResponse> out = new ArrayList<>();
        for (Comment c : list) {
            CommentListResponse dto = new CommentListResponse();
            dto.setId(c.getId());
            dto.setContent(c.getContent());
            dto.setUsername(c.getUser().getUsername());
            dto.setCreatedAt(c.getCreatedAt());
            out.add(dto);
        }
        return out;
    }

    @Override
    public CommentResponseDto updateComment(long commentId, CommentUpdateRequestDto dto) {
        User me = getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Yorum bulunamadı."));

        if (comment.getUser().getId() != me.getId()) {
            throw new AccessDeniedException("Sadece kendi yorumunu güncelleyebilirsin.");
        }

        comment.setContent(dto.getContent());
        commentRepository.save(comment);

        CommentResponseDto resp = new CommentResponseDto();
        resp.setId(comment.getId());
        resp.setContent(comment.getContent());
        resp.setUsername(comment.getUser().getUsername());
        resp.setCreatedAt(comment.getCreatedAt());
        return resp;
    }
}