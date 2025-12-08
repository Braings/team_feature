package com.bridgeX.facilities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacilitiesRepository extends JpaRepository<FacilitiesInfo, Long> {
	Optional<FacilitiesInfo> findById(long id);
	List<FacilitiesInfo> findByFcltyManageCtprvnNmAndFcltyManageSignguNm(
            String ctprvnNm,
            String signguNm
    );
	
	@Query(value = """
	        SELECT *
	        FROM public_fclty
	        WHERE FCLTY_MANAGE_CTPRVN_NM LIKE CONCAT('%', :region, '%')
	          AND FCLTY_MANAGE_SIGNGU_NM LIKE CONCAT('%', :city, '%')
	        """, nativeQuery = true)
	    List<FacilitiesInfo> searchByRegionAndCity(
	            @Param("region") String region,
	            @Param("city") String city);
}
