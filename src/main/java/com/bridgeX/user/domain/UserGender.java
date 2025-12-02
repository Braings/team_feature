package com.bridgeX.user.domain;

import lombok.Getter;

@Getter
public enum UserGender {	
	MALE("MALE"), FEMALE("FEMALE");
	
	UserGender(String value) {
		this.value = value;
	}
	
	private String value;

}
