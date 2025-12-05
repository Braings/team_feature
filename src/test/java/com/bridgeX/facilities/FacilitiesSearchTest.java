package com.bridgeX.facilities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FacilitiesSearchTest {

    @Autowired
    private FacilitiesService facilitiesService;

    @Test
    void search_real_db() {

        // given
        FacilitiesRequeest req = new FacilitiesRequeest();
        req.setFCLTY_MANAGE_CTPRVN_NM("충청북도");
        req.setFCLTY_MANAGE_SIGNGU_NM("청주시 서원구");

        // when
        List<FacilitiesInfo> result = facilitiesService.searchFacilities(req);

        // then
        // 최소 1개 이상 존재해야 함
        assertThat(result.size()).isGreaterThan(0);

        // 모든 데이터가 조건에 맞아야 함
        assertThat(result)
                .allMatch(f ->
                        "충청북도".equals(f.getFcltyManageCtprvnNm()) &&
                        "청주시 서원구".equals(f.getFcltyManageSignguNm())
                );
    }
}
