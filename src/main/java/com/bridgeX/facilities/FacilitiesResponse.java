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
    
    
    public static FacilitiesResponse from(FacilitiesInfo info) {
        return FacilitiesResponse.builder()
                .id(info.getId())
                .FCLTY_NM(info.getFCLTY_NM())
                .INDUTY_NM(info.getINDUTY_NM())
                .ROAD_NM_ZIP_NO(info.getROAD_NM_ZIP_NO())
                .RDNMADR_ONE_NM(info.getRDNMADR_ONE_NM())
                .RDNMADR_TWO_NM(info.getRDNMADR_TWO_NM())
                .ZIP_NO_VALUE(info.getZIP_NO_VALUE())
                .FCLTY_ADDR_ONE_NM(info.getFCLTY_ADDR_ONE_NM())
                .FCLTY_ADDR_TWO_NM(info.getFCLTY_ADDR_TWO_NM())
                .FCLTY_TEL_NO(info.getFCLTY_TEL_NO())
                .RSPNSBLTY_TEL_NO(info.getRSPNSBLTY_TEL_NO())
                .FCLTY_HMPG_URL(info.getFCLTY_HMPG_URL())
                .FCLTY_MANAGE_CTPRVN_NM(info.getFcltyManageCtprvnNm())
                .FCLTY_MANAGE_SIGNGU_NM(info.getFcltyManageSignguNm())
                .build();
    }
}
