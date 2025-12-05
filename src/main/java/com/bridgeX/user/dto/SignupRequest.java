package com.bridgeX.user.dto;

import java.time.LocalDate;

import com.bridgeX.user.domain.UserGender;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Sign-up Request DTO
public class SignupRequest {
	// @NotEmpty(message = "ID는 공백으로 둘 수 없습니다.") // 프론트엔드 미구현으로 임시 주석 처리 (공백 시 오류 방지)
	
	private String nickname;
	
	@NotEmpty(message = "[SERVER] ID는 공백으로 둘 수 없습니다.")
	private String username;

	@NotEmpty(message = "[SERVER] 비밀번호는 공백으로 둘 수 없습니다.")
	private String password;

	@NotNull
	private LocalDate birthday;

	@NotNull
	private UserGender sex;

	@NotEmpty(message = "[SERVER] 이메일은 공백으로 둘 수 없습니다.")
	@Email(message = "[SERVER] 잘못된 이메일 형식입니다.")
	private String email;

	@Min(value = 1, message = "[SERVER] 키는 0 이하일 수 없습니다.")
	private Integer height;

	@Min(value = 1, message = "[SERVER] 체중은 0 이하일 수 없습니다.")
	private Integer weight;
}
