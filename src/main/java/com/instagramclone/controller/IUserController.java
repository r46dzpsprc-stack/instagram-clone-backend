package com.instagramclone.controller;

import java.util.List;

import com.instagramclone.dto.request.LoginRequestDto;
import com.instagramclone.dto.request.UserRequestDto;
import com.instagramclone.dto.response.LoginResponseDto;
import com.instagramclone.dto.response.UserListResponseDto;
import com.instagramclone.dto.response.UserResponseDto;

public interface IUserController {
	
	UserResponseDto register(UserRequestDto userRequestDto);
	
	LoginResponseDto login(LoginRequestDto loginRequestDto);
	
	public void deleteById(long id);
	
	public List<UserListResponseDto> getAllUsers();

}
