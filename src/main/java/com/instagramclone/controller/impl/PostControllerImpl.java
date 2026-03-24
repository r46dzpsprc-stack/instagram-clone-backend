package com.instagramclone.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagramclone.controller.IPostController;
import com.instagramclone.dto.request.PostRequestDto;
import com.instagramclone.dto.response.PostResponseDto;
import com.instagramclone.service.IPostService;

@RestController
@RequestMapping("/post")
public class PostControllerImpl implements IPostController {
	
	@Autowired
	
	
	private IPostService postService;

	@Override
	@PostMapping("/create")
	public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto) {
		return postService.createPost(postRequestDto);
	}

	@Override
	@GetMapping("/user/{userId}")
	public List<PostResponseDto> getPostUserId(@PathVariable(name = "userId") long userId) {
		return postService.getPostUserId(userId);
	}

	@Override
	@DeleteMapping("user/{userId}/delete/post/{index}")
	public void deletePostByIndex(@PathVariable(name = "userId") long userId,@PathVariable(name = "index")  int index) {
		postService.deletePostByIndex(userId, index);
	}

}
