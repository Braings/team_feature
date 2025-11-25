package com.bridgeX.user.dto;

import com.bridgeX.user.domain.UserGender;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBodyInfoRequest {
	@Min(1)
	private int height;
	
	@Min(1)
	private int weight;
	
	@NotNull
	private UserGender gender; // e-num { MALE, FEMALE }
	
	//TODO: Add Other fields
}
