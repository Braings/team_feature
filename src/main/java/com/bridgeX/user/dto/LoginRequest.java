package com.bridgeX.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

//Login Request DTO
@Getter
@Setter
public class LoginRequest {
	@NotEmpty(message = "ID는 공백으로 둘 수 없습니다.")
	private String username;
	
	@NotEmpty(message = "비밀번호는 공백으로 둘 수 없습니다.")
	private String password;
}