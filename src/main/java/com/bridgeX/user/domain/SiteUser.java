package com.bridgeX.user.domain;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SiteUser {

	// User ID
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Login ID
	@Column(unique = true)
	private String username;

	// NickName
	@Column(unique = true)
	private String nickname;
	
	private String password;

	@Column(unique = true)
	private String email;
	
	@Column(name = "birthday")
	private LocalDate birthday;
	
	@Enumerated(EnumType.STRING) // Use e-num
    private UserRole role;
	
	// Profile Photo
	private String profileImageUrl;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private SiteUserBody bodyInfo;
}
