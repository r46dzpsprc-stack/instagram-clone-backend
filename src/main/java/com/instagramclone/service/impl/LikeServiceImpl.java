package com.instagramclone.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.instagramclone.dto.response.UserLikeResponseDto;
import com.instagramclone.entity.Like;
import com.instagramclone.entity.Post;
import com.instagramclone.entity.User;
import com.instagramclone.repository.LikeRepository;
import com.instagramclone.repository.PostRepository;
import com.instagramclone.repository.UserRepository;
import com.instagramclone.service.ILikeService;

@Service
public class LikeServiceImpl implements ILikeService {

    @Autowired private LikeRepository likeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new AccessDeniedException("Giriş yapmalısın.");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AccessDeniedException("Kullanıcı bulunamadı."));
    }

    @Override
    public String likePost(long postId) {
        User user = getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Gönderi bulunamadı."));

        if (likeRepository.findByUserAndPost(user, post).isPresent()) {
            throw new RuntimeException("Bu gönderiyi zaten beğenmişsiniz.");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        return "Beğeni başarılı!";
    }

    @Override
    public long getLikeCountByPostId(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Gönderi bulunamadı."));
        return likeRepository.countByPost(post);
    }

    @Override
    public String unlikePost(long postId) {
        User user = getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Gönderi bulunamadı."));

        var like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("Bu gönderiyi beğenmemişsiniz."));
        likeRepository.delete(like);

        return "Beğeni kaldırıldı!";
    }

    @Override
    public List<UserLikeResponseDto> getUsersWhoLikedPost(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Gönderi bulunamadı."));

        List<Like> likes = likeRepository.findByPost(post);
        List<UserLikeResponseDto> result = new ArrayList<>();
        for (Like l : likes) {
            User u = l.getUser();
            result.add(new UserLikeResponseDto(
                    u.getId(),
                    u.getUsername(),
                    u.getProfileImageUrl()
            ));
        }
        return result;
    }
}