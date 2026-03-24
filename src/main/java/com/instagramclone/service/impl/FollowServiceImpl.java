package com.instagramclone.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instagramclone.dto.response.UserListResponseDto;
import com.instagramclone.entity.Follow;
import com.instagramclone.entity.User;
import com.instagramclone.repository.FollowRepository;
import com.instagramclone.repository.UserRepository;
import com.instagramclone.service.IFollowService;

@Service
public class FollowServiceImpl implements IFollowService {

    @Autowired private FollowRepository followRepository;
    @Autowired private UserRepository userRepository;

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new AccessDeniedException("Giriş yapmalısın.");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AccessDeniedException("Kullanıcı bulunamadı."));
    }

    private User getUserOrThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
    }

    @Override
    @Transactional
    public String followUser(long followerIdIgnored, long followedId) {
        User me = getCurrentUser();
        if (me.getId() == followedId) {
            throw new RuntimeException("Kendini takip edemezsin.");
        }

        User followed = getUserOrThrow(followedId);

        if (followRepository.existsByFollowerAndFollowed(me, followed)) {
            return "Zaten takip ediyorsun.";
        }

        Follow f = new Follow();
        f.setFollower(me);
        f.setFollowed(followed);
        followRepository.save(f);

        return "Takip işlemi başarılı!";
    }

    @Override
    @Transactional
    public String unfollowUser(long followerIdIgnored, long followedId) {
        User me = getCurrentUser();
        if (me.getId() == followedId) {
            throw new RuntimeException("Kendini takipten çıkaramazsın.");
        }

        User followed = getUserOrThrow(followedId);

        if (!followRepository.existsByFollowerAndFollowed(me, followed)) {
            return "Zaten takip etmiyorsun.";
        }

        followRepository.deleteByFollowerAndFollowed(me, followed);
        return "Takipten çıkarıldı!";
    }

    @Override
    public List<UserListResponseDto> getFollowers(long userId) {
        User user = getUserOrThrow(userId);
        List<Follow> followList = followRepository.findByFollowed(user);

        List<UserListResponseDto> out = new ArrayList<>();
        for (Follow f : followList) {
            User follower = f.getFollower();
            UserListResponseDto dto = new UserListResponseDto();
            dto.setId(follower.getId());
            dto.setUsername(follower.getUsername());
            out.add(dto);
        }
        return out;
    }

    @Override
    public List<UserListResponseDto> getFollowing(long userId) {
        User user = getUserOrThrow(userId);
        List<Follow> followList = followRepository.findByFollower(user);

        List<UserListResponseDto> out = new ArrayList<>();
        for (Follow f : followList) {
            User followed = f.getFollowed();
            UserListResponseDto dto = new UserListResponseDto();
            dto.setId(followed.getId());
            dto.setUsername(followed.getUsername());
            out.add(dto);
        }
        return out;
    }
}