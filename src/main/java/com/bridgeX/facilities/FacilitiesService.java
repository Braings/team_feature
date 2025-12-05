package com.bridgeX.facilities;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacilitiesService {
	
	private final FacilitiesRepository facilitiesRepository;

	// 일단 혹시 몰라서 전체 가져오기 만들어둠
    public List<FacilitiesResponse> getAllFacilities() {
        return facilitiesRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
	
    // 시군구 입력 값 기반 검색
    public List<FacilitiesResponse> searchFacilities(String region, String city) {
        System.out.println(">>> service 입력: region=" + region + ", city=" + city);

        List<FacilitiesInfo> result =
                facilitiesRepository.searchByRegionAndCity(region, city);

        System.out.println(">>> service 결과 개수 = " + result.size());

        return result.stream()
                .map(FacilitiesResponse::from) // 혹은 직접 매핑
                .toList();
    }
    /*
    public List<FacilitiesResponse> searchFacilities(String region, String city) {
        return facilitiesRepository.findByFcltyManageCtprvnNmAndFcltyManageSignguNm(region, city)
            .stream().map(FacilitiesResponse::from)
            .toList();
    }
	*/

    
	// 하나만 검색
	public FacilitiesResponse getFacility(Long id) {
        FacilitiesInfo entity = facilitiesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[SERVER]: 시설을 찾을 수 없습니다. id=" + id));
        return toResponse(entity);
    }
	
	private FacilitiesResponse toResponse(FacilitiesInfo entity) {
	    return FacilitiesResponse.builder()
	        .id(entity.getId())
	        .FCLTY_NM(entity.getFCLTY_NM())
	        .INDUTY_NM(entity.getINDUTY_NM())
	        .ROAD_NM_ZIP_NO(entity.getROAD_NM_ZIP_NO())
	        .RDNMADR_ONE_NM(entity.getRDNMADR_ONE_NM())
	        .RDNMADR_TWO_NM(entity.getRDNMADR_TWO_NM())
	        .ZIP_NO_VALUE(entity.getZIP_NO_VALUE())
	        .FCLTY_ADDR_ONE_NM(entity.getFCLTY_ADDR_ONE_NM())
	        .FCLTY_ADDR_TWO_NM(entity.getFCLTY_ADDR_TWO_NM())
	        .FCLTY_TEL_NO(entity.getFCLTY_TEL_NO())
	        .RSPNSBLTY_TEL_NO(entity.getRSPNSBLTY_TEL_NO())
	        .FCLTY_HMPG_URL(entity.getFCLTY_HMPG_URL())
	        .FCLTY_MANAGE_CTPRVN_NM(entity.getFcltyManageCtprvnNm())
	        .FCLTY_MANAGE_SIGNGU_NM(entity.getFcltyManageSignguNm())
	        //.FCLTY_MANAGE_CTPRVN_NMElement(entity.getFCLTY_MANAGE_CTPRVN_NMElement())
	        .build();
	}

}
