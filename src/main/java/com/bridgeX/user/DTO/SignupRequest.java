package com.bridgeX.user.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//Sign-up Request DTO
public class SignupRequest {
	private String username;
	private String password;
	private String email;
}
