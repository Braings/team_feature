package com.bridgeX.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SiteUserBody {
	@Id
    private Long id;
	
	@OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private SiteUser user;
	
	private int height;
	private int weight;
	
	@Enumerated(EnumType.STRING) // Use e-num
    private UserGender gender;
	
}
