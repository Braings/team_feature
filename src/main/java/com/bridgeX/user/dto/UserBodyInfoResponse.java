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
	
	private Integer user_grip;
	private Integer user_flex;
	private Integer user_situp;
	private Integer user_jump;
	
	// Can add Other fields
	public static UserBodyInfoResponse from(SiteUserBody body) {
        if (body == null) {
            return null; // or 기본값 builder()
        }

        return UserBodyInfoResponse.builder()
                .gender(body.getGender().name())
                .height(body.getHeight())
                .weight(body.getWeight())
                .user_grip(body.getUser_grip())
                .user_flex(body.getUser_flex())
                .user_situp(body.getUser_situp())
                .user_jump(body.getUser_jump())
                .build();
    }
    
}
