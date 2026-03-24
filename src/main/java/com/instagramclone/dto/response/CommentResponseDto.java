package com.instagramclone.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
	
	private long id;
	
	private String content;
	
	private String username;
	
	private LocalDateTime createdAt;
	

}
