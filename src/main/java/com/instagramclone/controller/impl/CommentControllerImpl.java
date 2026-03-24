package com.instagramclone.controller.impl;

import com.instagramclone.controller.ICommentController;
import com.instagramclone.dto.request.CommentRequestDto;
import com.instagramclone.dto.request.CommentUpdateRequestDto;
import com.instagramclone.dto.response.CommentListResponse;
import com.instagramclone.dto.response.CommentResponseDto;
import com.instagramclone.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentControllerImpl implements ICommentController {

    @Autowired
    private ICommentService commentService;

    @PostMapping("/add")
    public CommentResponseDto addComment(@RequestBody CommentRequestDto dto) {
        return commentService.addComment(dto);
    }

    @Override
    @GetMapping("/list")
    public List<CommentListResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @Override
    @DeleteMapping("/delete/{commentId}")
    public void deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
    }

    @Override
    @GetMapping("/post/{postId}")
    public List<CommentListResponse> getCommentsByPostId(@PathVariable long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @Override
    @PutMapping("/update/{commentId}")
    public CommentResponseDto updateComment(@PathVariable long commentId,
                                            @RequestBody CommentUpdateRequestDto dto) {
        return commentService.updateComment(commentId, dto);
    }
}