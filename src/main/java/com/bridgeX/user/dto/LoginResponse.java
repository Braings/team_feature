package com.bridgeX.user.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//Login Response DTO
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
	private boolean success;
	private String message;
// TODO: 나중에 토큰이나 유저 정보도 추가 가능
}