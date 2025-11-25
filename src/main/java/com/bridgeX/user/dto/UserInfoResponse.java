package com.bridgeX.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// UserInfo Response DTO
public class UserInfoResponse {
	private long id;
	private String username;		
	private String email;
	private String role;
}
