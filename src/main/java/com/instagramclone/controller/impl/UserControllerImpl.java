package com.instagramclone.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.instagramclone.controller.IUserController;
import com.instagramclone.dto.request.LoginRequestDto;
import com.instagramclone.dto.request.UserRequestDto;
import com.instagramclone.dto.response.LoginResponseDto;
import com.instagramclone.dto.response.UserListResponseDto;
import com.instagramclone.dto.response.UserResponseDto;
import com.instagramclone.service.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserControllerImpl implements IUserController {

    @Autowired
    private IUserService userService;

    @Override
    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRequestDto userRequestDto) {
        return userService.register(userRequestDto);
    }

    @Override
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe() {
        userService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable(name = "id") long id) {
        userService.deleteById(id);
    }

    @Override
    @GetMapping("/list")
    public List<UserListResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }
}