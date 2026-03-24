package com.instagramclone.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.instagramclone.dto.request.LoginRequestDto;
import com.instagramclone.dto.request.UserRequestDto;
import com.instagramclone.dto.response.LoginResponseDto;
import com.instagramclone.dto.response.UserListResponseDto;
import com.instagramclone.dto.response.UserResponseDto;
import com.instagramclone.entity.User;
import com.instagramclone.repository.UserRepository;
import com.instagramclone.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // BCrypt

    @Override
    public UserResponseDto register(UserRequestDto userRequestDto) {
        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setBio(userRequestDto.getBio());
        user.setProfileImageUrl(userRequestDto.getProfileImageUrl());
        user.setPlainPassword(userRequestDto.getPassword());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        User savedUser = userRepository.save(user);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(savedUser.getId());
        responseDto.setUsername(savedUser.getUsername());
        responseDto.setBio(savedUser.getBio());
        responseDto.setProfileImageUrl(savedUser.getProfileImageUrl());
        return responseDto;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String rawPassword = loginRequestDto.getPassword();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Kullanıcı bulunamadı.");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Şifre hatalı.");
        }

        LoginResponseDto response = new LoginResponseDto();
        response.setUsername(user.getUsername());
        return response;
    }

    @Override
    public void deleteById(long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Kullanıcı bulunamadı.");
        }
    }

    @Override
    public void deleteCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new AccessDeniedException("Giriş yapmalısın.");
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
        userRepository.delete(user);
    }

    @Override
    public List<UserListResponseDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserListResponseDto> userListDto = new ArrayList<>();
        for (User user : userList) {
            UserListResponseDto dto = new UserListResponseDto();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            userListDto.add(dto);
        }
        return userListDto;
    }
}