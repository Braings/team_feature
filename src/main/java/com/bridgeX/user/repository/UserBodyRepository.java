package com.bridgeX.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgeX.user.domain.SiteUser;
import com.bridgeX.user.domain.SiteUserBody;

public interface UserBodyRepository extends JpaRepository<SiteUserBody, Long> {
	Optional<SiteUserBody> findByUser_Username(String username);
    Optional<SiteUserBody> findByUser(SiteUser user);
}
