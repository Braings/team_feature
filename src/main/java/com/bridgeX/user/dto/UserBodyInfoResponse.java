package com.bridgeX.user.dto;

import com.bridgeX.user.domain.SiteUserBody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserBodyInfoResponse {
	private Integer height;
	private Integer weight;
	private String gender; // e-num { MALE, FEMALE }
	
	//TODO: Add Other fields
	
	public static UserBodyInfoResponse from(SiteUserBody body) {
        if (body == null) {
            return null; // or 기본값 builder()
        }

        return UserBodyInfoResponse.builder()
                .gender(body.getGender().name())
                .height(body.getHeight())
                .weight(body.getWeight())
                .build();
    }
}
