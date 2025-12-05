package com.bridgeX.facilities;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exerciseFacilities") // 기본 Mapping 주소
@RequiredArgsConstructor
public class FacilitiesController {

    private final FacilitiesService facilitiesService;
    
    // 전체 조회(일단 혹시 몰라서 만들어 둠)
    @GetMapping("/all")
    public ResponseEntity<List<FacilitiesResponse>> getAllFacilities() {
        return ResponseEntity.ok(facilitiesService.getAllFacilities());
    }
    
    @GetMapping
    public ResponseEntity<List<FacilitiesResponse>> search(
            @RequestParam String region,
            @RequestParam String city
    ) {
        List<FacilitiesResponse> list = facilitiesService.searchFacilities(region, city);
        return ResponseEntity.ok(list);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<FacilitiesResponse> getFacility(@PathVariable Long id) {
        FacilitiesResponse response = facilitiesService.getFacility(id);
        return ResponseEntity.ok(response);
    }
}

