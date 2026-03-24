package com.instagramclone.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.instagramclone.dto.request.PostRequestDto;
import com.instagramclone.dto.response.PostResponseDto;
import com.instagramclone.entity.Post;
import com.instagramclone.entity.User;
import com.instagramclone.repository.PostRepository;
import com.instagramclone.repository.UserRepository;
import com.instagramclone.service.IPostService;

@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new AccessDeniedException("Giriş yapmalısın.");
        }
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("Kullanıcı bulunamadı."));
    }

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        User user = getCurrentUser();

        Post post = new Post();
        post.setContent(postRequestDto.getContent());
        post.setImageUrl(postRequestDto.getImageUrl());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);

        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUsername(user.getUsername());
        return dto;
    }

    @Override
    public List<PostResponseDto> getPostUserId(long userId) {
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

        List<Post> posts = postRepository.findAllByUser(target);
        List<PostResponseDto> list = new ArrayList<>();

        for (Post post : posts) {
            PostResponseDto dto = new PostResponseDto();
            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setUsername(post.getUser().getUsername());
            list.add(dto);
        }
        return list;
    }

    @Override
    public void deletePostByIndex(long userId, int index) {
        User current = getCurrentUser();

        List<Post> posts = postRepository.findAllByUser(current);
        if (index < 0 || index >= posts.size()) {
            throw new RuntimeException("Geçersiz post index!");
        }

        postRepository.delete(posts.get(index));
    }
}