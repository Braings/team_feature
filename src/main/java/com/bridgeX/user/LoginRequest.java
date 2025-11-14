package com.bridgeX.user;

import lombok.Getter;
import lombok.Setter;

//Login Request DTO
@Getter
@Setter
public class LoginRequest {
private String username;
private String password;
}