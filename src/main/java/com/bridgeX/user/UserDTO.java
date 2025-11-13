package com.bridgeX.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Signup Request DTO
@Getter
@Setter
public class SignupRequest {
 private String username;
 private String password;
 private String email;
}

// Login Request DTO
@Getter
@Setter
public class LoginRequest {
 private String username;
 private String password;
}

// Login Response DTO
@Getter @Setter
@AllArgsConstructor
public class LoginResponse {
 private boolean success;
 private String message;
 // TODO: 나중에 토큰이나 유저 정보도 추가 가능
}
