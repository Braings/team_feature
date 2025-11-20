package com.bridgeX.user.dto;

// backend test only

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
	@Size(min = 1, max = 25, message = "ID는 25자 내에서만 가능합니다.")
	@NotEmpty(message = "ID는 공백으로 둘 수 없습니다.")
	private String username;

	@Size(min = 4, max = 25, message = "비밀번호는 4 ~ 25자 내에서만 가능합니다.")
	@NotEmpty(message = "비밀번호는 공백으로 둘 수 없습니다.")
	private String password1;

	@NotEmpty(message = "비밀번호 확인란은 공백으로 둘 수 없습니다")
	private String password2;

	@NotEmpty(message = "이메일은 공백으로 둘 수 없습니다.")
	@Email(message = "잘못된 이메일 형식입니다.")
	private String email;
}
