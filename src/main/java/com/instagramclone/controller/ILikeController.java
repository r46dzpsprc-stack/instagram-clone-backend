package com.instagramclone.controller;

import java.util.List;

import com.instagramclone.dto.response.UserLikeResponseDto;

public interface ILikeController {
    String likePost(long postId);
    
    long getLikeCountByPostId(long postId);
    
    String unlikePost(long postId);
    
    List<UserLikeResponseDto> getUsersWhoLikedPost(long postId);
}