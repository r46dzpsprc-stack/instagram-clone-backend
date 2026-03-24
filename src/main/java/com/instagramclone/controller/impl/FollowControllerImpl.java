package com.instagramclone.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.instagramclone.controller.IFollowController;
import com.instagramclone.dto.response.UserListResponseDto;
import com.instagramclone.service.IFollowService;

@RestController
@RequestMapping("/follow")
public class FollowControllerImpl implements IFollowController {

    @Autowired
    private IFollowService followService;
    
    @Override
    @PostMapping("/{followedId}")
    public String followUser(@PathVariable long followedId) {
        return followService.followUser(0L, followedId);
    }

    @Override
    @DeleteMapping("/{followedId}")
    public String unfollowUser(@PathVariable long followedId) {
        return followService.unfollowUser(0L, followedId);
    }

    @Override
    @GetMapping("/followers/{userId}")
    public List<UserListResponseDto> getFollowers(@PathVariable long userId) {
        return followService.getFollowers(userId);
    }

    @Override
    @GetMapping("/following/{userId}")
    public List<UserListResponseDto> getFollowing(@PathVariable long userId) {
        return followService.getFollowing(userId);
    }
}