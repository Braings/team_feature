package com.bridgeX.user.dto;

import com.bridgeX.user.domain.UserGender;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBodyInfoRequest {
	@Min(1)
	private Integer height;

	@Min(1)
	private Integer weight;

	@NotNull
	private UserGender gender; // e-num { MALE, FEMALE }

	private Integer user_grip;
	
	private Integer user_flex;
	
	private Integer user_situp;
	
	private Integer user_jump;
	
	// TODO: Add Other fields
}
