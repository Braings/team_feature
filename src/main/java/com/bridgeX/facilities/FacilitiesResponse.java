package com.bridgeX.facilities;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacilitiesResponse {
	
    private Long id;
    
    private String FCLTY_NM; // 청주종합경기장
    
    private String INDUTY_NM; // 종합경기장 (축구장, 트랙)
    
    private String ROAD_NM_ZIP_NO; // 28639
    
    private String RDNMADR_ONE_NM; // 충북 청주시 서원구 사직대로 229
    
    private String RDNMADR_TWO_NM; // 일부 시설별 상이
    
    private String ZIP_NO_VALUE; // 28639
    
    private String FCLTY_ADDR_ONE_NM; // 충북 청주시 서원구 사직동 229-1
    
    private String FCLTY_ADDR_TWO_NM; // 일부 시설별 상이
    
    private String FCLTY_TEL_NO; // 043-281-2222
    
    private String RSPNSBLTY_TEL_NO; // 043-281-2223
    
    private String FCLTY_HMPG_URL; // http://www.cheongju.go.kr
    
    private String FCLTY_MANAGE_CTPRVN_NM; // 충청북도
    
    private String FCLTY_MANAGE_SIGNGU_NM; // 청주시 서원구
    
    // private String FCLTY_MANAGE_CTPRVN_NMElement; // 충북

}
