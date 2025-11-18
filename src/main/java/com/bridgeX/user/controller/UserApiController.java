package com.bridgeX.user.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeX.user.SiteUser;
import com.bridgeX.user.UserService;
import com.bridgeX.user.dto.LoginRequest;
import com.bridgeX.user.dto.LoginResponse;
import com.bridgeX.user.dto.SignupRequest;
import com.bridgeX.user.dto.UserInfoResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//This is API communication controller

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    
    // Sign-up
    @PostMapping("/sign")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest dto) {
        userService.signup(dto);
        return ResponseEntity.ok("signup success");
    }
    
    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest dto) {
        LoginResponse result = userService.login(dto);  // Check login success/fail
        return ResponseEntity.ok(result);
    }
    
    // User Info Response
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(Principal principal) {
        if (principal == null) {
            // 로그인 안 됐을 때
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = principal.getName();   // 여기서 로그인한 사람 user name get
        SiteUser user = userService.getUserByUsername(username);

        UserInfoResponse dto = new UserInfoResponse(
                user.getUsername(),
                user.getEmail()
        );

        return ResponseEntity.ok(dto);
    }

}

