package com.bridgeX.user.dto;

import java.time.LocalDate;

import com.sun.istack.NotNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//Sign-up Request DTO
public class SignupRequest {
	@NotEmpty(message = "ID는 공백으로 둘 수 없습니다.")
	@Size(min = 1, max = 25, message = "ID는 25자 내에서만 가능합니다.")
	private String username;
	
	@NotEmpty(message = "비밀번호는 공백으로 둘 수 없습니다.")
	@Size(min = 4, max = 25, message = "비밀번호는 4 ~ 25자 내에서만 가능합니다.")
	private String password;
	
	@NotEmpty(message = "비밀번호 확인은 필수입니다.")
	@Size(min = 4, max = 25, message = "비밀번호는 4 ~ 25자 내에서만 가능합니다.")
	private String passwordCheck;
	
	@NotNull
	private LocalDate birthday;
	
	@NotEmpty(message = "이메일은 공백으로 둘 수 없습니다.")
	@Email(message = "잘못된 이메일 형식입니다.")
	private String email;
}
