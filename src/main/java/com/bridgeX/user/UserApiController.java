package com.bridgeX.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

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

