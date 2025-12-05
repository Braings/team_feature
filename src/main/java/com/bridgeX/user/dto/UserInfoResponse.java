package com.bridgeX.user.dto;

import java.time.LocalDate;

import com.bridgeX.user.domain.SiteUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
// UserInfo Response DTO
public class UserInfoResponse {
	private long id;
	private String nickname;
	private String username;		
	private String email;
	private LocalDate birthday;
	private String role;
	
	
	public static UserInfoResponse from(SiteUser user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .role(user.getRole().name())
                .build();
    }
}
