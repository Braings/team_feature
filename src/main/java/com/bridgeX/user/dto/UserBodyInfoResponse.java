package com.bridgeX.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserBodyInfoResponse {
	private int height;
	private int weight;
	private String gender; // e-num { MALE, FEMALE }
	
	//TODO: Add Other fields
}
