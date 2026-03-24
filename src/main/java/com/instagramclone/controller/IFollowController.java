package com.instagramclone.controller;

import java.util.List;

import com.instagramclone.dto.response.UserListResponseDto;

public interface IFollowController {
	
	
	String followUser(long followedId);
	String unfollowUser(long followedId);
	List<UserListResponseDto> getFollowers(long userId);
	List<UserListResponseDto> getFollowing(long userId);

}
