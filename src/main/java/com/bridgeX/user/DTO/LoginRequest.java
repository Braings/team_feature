package com.bridgeX.user.dto;

import lombok.Getter;
import lombok.Setter;

//Login Request DTO
@Getter
@Setter
public class LoginRequest {
private String username;
private String password;
}