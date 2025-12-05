package com.bridgeX.facilities;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exerciseFacilities") // 기본 Mapping 주소
@RequiredArgsConstructor
public class FacilitiesController {

    private final FacilitiesService facilitiesService;

    // 전체 조회(일단 혹시 몰라서 만들어 둠)
    @GetMapping
    public ResponseEntity<List<FacilitiesResponse>> getAllFacilities() {
        List<FacilitiesResponse> list = facilitiesService.getAllFacilities();
        return ResponseEntity.ok(list);
    }
    
    @PostMapping
    public List<FacilitiesInfo> search(@RequestBody FacilitiesRequeest req) {
        return facilitiesService.searchFacilities(req);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<FacilitiesResponse> getFacility(@PathVariable Long id) {
        FacilitiesResponse response = facilitiesService.getFacility(id);
        return ResponseEntity.ok(response);
    }
}

