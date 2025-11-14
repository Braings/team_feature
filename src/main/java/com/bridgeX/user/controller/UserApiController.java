package com.bridgeX.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeX.user.UserService;
import com.bridgeX.user.dto.LoginRequest;
import com.bridgeX.user.dto.LoginResponse;
import com.bridgeX.user.dto.SignupRequest;

import lombok.RequiredArgsConstructor;

//This is API communication controller

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    // Sign-up
    @PostMapping("/sign")
    public ResponseEntity<?> signup(@RequestBody SignupRequest dto) {
        userService.signup(dto);
        return ResponseEntity.ok("signup success");
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        LoginResponse result = userService.login(dto);  // Check login success/fail
        return ResponseEntity.ok(result);
    }
}

