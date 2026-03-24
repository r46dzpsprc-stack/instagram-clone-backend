package com.instagramclone.service;

import java.util.List;

import com.instagramclone.dto.response.UserListResponseDto;

public interface IFollowService {
	
	public String followUser(long followerId,long followedId);
	
	public String unfollowUser(long followerId,long followedId);
	
	public List<UserListResponseDto> getFollowers(long userId);
	
	public List<UserListResponseDto> getFollowing(long userId);

}
