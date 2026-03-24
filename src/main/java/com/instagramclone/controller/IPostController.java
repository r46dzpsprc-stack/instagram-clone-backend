package com.instagramclone.controller;

import java.util.List;

import com.instagramclone.dto.request.PostRequestDto;
import com.instagramclone.dto.response.PostResponseDto;

public interface IPostController {
	
public PostResponseDto createPost(PostRequestDto postRequestDto);
	
	public List<PostResponseDto> getPostUserId(long userId);
	
	void deletePostByIndex(long userId, int index);

}
