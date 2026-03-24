package com.instagramclone.controller;

import java.util.List;

import com.instagramclone.dto.request.CommentRequestDto;
import com.instagramclone.dto.request.CommentUpdateRequestDto;
import com.instagramclone.dto.response.CommentListResponse;
import com.instagramclone.dto.response.CommentResponseDto;

public interface ICommentController {
	
	public CommentResponseDto addComment(CommentRequestDto dto);
	
	public List<CommentListResponse> getAllComments();
	
	public void deleteComment(long commentId);
	
	public List<CommentListResponse> getCommentsByPostId(long postId);
	
	public CommentResponseDto updateComment(long commentId,CommentUpdateRequestDto dto);

}
