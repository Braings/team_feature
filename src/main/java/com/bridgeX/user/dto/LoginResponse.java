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
	private String token;
	// private String message; // 프론트와 에러 처리가 겹쳐 비활성화
	// TODO: 나중에 토큰이나 유저 정보도 추가 가능
}