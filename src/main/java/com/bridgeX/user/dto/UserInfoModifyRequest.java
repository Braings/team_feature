package com.bridgeX.user.dto;

import java.time.LocalDate;

import com.bridgeX.user.domain.UserGender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfoModifyRequest {
	// 입력이 없는 대상은 변경하지 않음
	
	@Size(min = 1, max = 25, message = "[SERVER] 닉네임은 25자 내에서만 가능합니다.")
	private String nickname;

	@Size(min = 4, max = 25, message = "[SERVER] 비밀번호는 4 ~ 25자 내에서만 가능합니다.")
	private String password;

	private LocalDate birthday;

	private UserGender sex;

	@Email(message = "[SERVER] 잘못된 이메일 형식입니다.")
	private String email;

	@Min(value = 1, message = "[SERVER] 키는 0 이하일 수 없습니다.")
	private Integer height;

	@Min(value = 1, message = "[SERVER] 체중은 0 이하일 수 없습니다.")
	private Integer weight;
	
	private Integer user_grip;
	
	private Integer user_flex;
	
	private Integer user_situp;
	
	private Integer user_jump;
}
