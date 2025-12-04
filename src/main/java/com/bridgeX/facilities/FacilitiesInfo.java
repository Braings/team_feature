package com.bridgeX.facilities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "public_fclty")
public class FacilitiesInfo {

	/* 각 항목별 주석은 데이터 예시 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 50)
    private String FCLTY_NM; // 청주종합경기장
    
    @Column(length = 200)
    private String INDUTY_NM; // 종합경기장 (축구장, 트랙)
    
    @Column(length = 5)
    private String ROAD_NM_ZIP_NO; // 28639
    
    @Column(length = 200)
    private String RDNMADR_ONE_NM; // 충북 청주시 서원구 사직대로 229
    
    @Column(length = 200)
    private String RDNMADR_TWO_NM; // 일부 시설별 상이
    
    @Column(length = 5)
    private String ZIP_NO_VALUE; // 28639
    
    @Column(length = 200)
    private String FCLTY_ADDR_ONE_NM; // 충북 청주시 서원구 사직동 229-1
    
    @Column(length = 200)
    private String FCLTY_ADDR_TWO_NM; // 일부 시설별 상이
    
    @Column(length = 20)
    private String FCLTY_TEL_NO; // 043-281-2222
    
    @Column(length = 20)
    private String RSPNSBLTY_TEL_NO; // 043-281-2223
    
    @Column(length = 200)
    private String FCLTY_HMPG_URL; // http://www.cheongju.go.kr
    
    @Column(length = 20, name = "FCLTY_MANAGE_CTPRVN_NM")
    private String fcltyManageCtprvnNm; // 충청북도
    
    @Column(length = 20, name = "FCLTY_MANAGE_SIGNGU_NM")
    private String fcltyManageSignguNm; // 청주시 서원구
    /*
    @Column(length = 20)
    private String FCLTY_MANAGE_CTPRVN_NMElement; // 충북
    */
}
