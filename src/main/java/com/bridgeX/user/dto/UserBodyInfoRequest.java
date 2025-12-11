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
	private Double height;

	@Min(1)
	private Double weight;

	@NotNull
	private UserGender gender; // e-num { MALE, FEMALE }

	private Double user_grip;
	
	private Double user_flex;
	
	private Integer user_situp;
	
	private Double user_jump;
	
	// TODO: Add Other fields
}
