package com.bridgeX.facilities;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class FacilitiesRepositoryTest {

    @Autowired
    FacilitiesRepository facilitiesRepository;

    @Test
    void testFindId1() {
        Optional<FacilitiesInfo> info = facilitiesRepository.findById(1L);

        System.out.println("==== ID 1 Result ====");
        System.out.println(info.orElse(null));

        assert(info.isPresent()); // 데이터 존재해야 함
    }

    @Test
    void testFindId2() {
        Optional<FacilitiesInfo> info = facilitiesRepository.findById(2L);

        System.out.println("==== ID 2 Result ====");
        System.out.println(info.orElse(null));

        assert(info.isPresent()); // 데이터 존재 확인
    }
}
