package com.bridgeX.facilities;

import lombok.Getter;
import lombok.Setter;

/* 시설 정보를 찾기 위해 입력되는 데이터 */

@Getter
@Setter
public class FacilitiesRequeest {
	private String FCLTY_MANAGE_CTPRVN_NM; // 충청북도
    private String FCLTY_MANAGE_SIGNGU_NM; // 청주시 서원구
}
