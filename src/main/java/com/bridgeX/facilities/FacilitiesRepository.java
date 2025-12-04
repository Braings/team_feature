package com.bridgeX.facilities;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilitiesRepository extends JpaRepository<FacilitiesInfo, Long> {
	Optional<FacilitiesInfo> findById(long id);
}
