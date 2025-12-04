package com.bridgeX.facilities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilitiesRepository extends JpaRepository<FacilitiesInfo, Long> {
	Optional<FacilitiesInfo> findById(long id);
	List<FacilitiesInfo> findByFcltyManageCtprvnNmAndFcltyManageSignguNm(
            String ctprvnNm,
            String signguNm
    );
}
