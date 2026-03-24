package com.instagramclone.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.instagramclone.controller.ILikeController;
import com.instagramclone.dto.response.UserLikeResponseDto;
import com.instagramclone.service.ILikeService;

@RestController
@RequestMapping("/like")
public class LikeControllerImpl implements ILikeController {

    @Autowired
    private ILikeService likeService;


    @Override
    @PostMapping("/{postId}")
    public String likePost(@PathVariable long postId) {
        return likeService.likePost(postId);
    }


    @Override
    @DeleteMapping("/unlike/{postId}")
    public String unlikePost(@PathVariable long postId) {
        return likeService.unlikePost(postId);
    }

    @Override
    @GetMapping("/count/{postId}")
    public long getLikeCountByPostId(@PathVariable long postId) {
        return likeService.getLikeCountByPostId(postId);
    }

    @Override
    @GetMapping("/liked-users/{postId}")
    public List<UserLikeResponseDto> getUsersWhoLikedPost(@PathVariable long postId) {
        return likeService.getUsersWhoLikedPost(postId);
    }
}