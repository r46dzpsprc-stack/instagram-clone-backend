package com.instagramclone.controller;

import com.instagramclone.dto.request.LoginRequestDto;
import com.instagramclone.dto.request.UserRequestDto;
import com.instagramclone.dto.response.LoginResponseDto;
import com.instagramclone.entity.User;
import com.instagramclone.repository.UserRepository;
import com.instagramclone.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto req) {
        userRepository.findByUsername(req.getUsername()).ifPresent(u -> {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor.");
        });

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setBio(req.getBio());
        user.setProfileImageUrl(req.getProfileImageUrl());
        user.setPlainPassword(req.getPassword());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        userRepository.save(user);
        UserDetails ud = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Collections.emptyList()
        );
        String token = jwtService.generateToken(ud);

        LoginResponseDto resp = new LoginResponseDto();
        resp.setToken(token);
        resp.setUsername(user.getUsername());
        resp.setUserId(user.getId());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto req) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
            authenticationManager.authenticate(authToken);

            User user = userRepository.findByUsername(req.getUsername())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

            UserDetails ud = new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), Collections.emptyList()
            );
            String token = jwtService.generateToken(ud);

            LoginResponseDto resp = new LoginResponseDto();
            resp.setToken(token);
            resp.setUsername(user.getUsername());
            resp.setUserId(user.getId());
            return ResponseEntity.ok(resp);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Kullanıcı adı veya şifre hatalı.");
        }
    }
}