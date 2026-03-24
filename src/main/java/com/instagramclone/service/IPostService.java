package com.instagramclone.service;

import java.util.List;

import com.instagramclone.dto.request.PostRequestDto;
import com.instagramclone.dto.response.PostResponseDto;

public interface IPostService {
		
    public PostResponseDto createPost(PostRequestDto postRequestDto);
	
	public List<PostResponseDto> getPostUserId(long userId);
	
	void deletePostByIndex(long userId, int index);

}
