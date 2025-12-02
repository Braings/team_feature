package com.bridgeX.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "id")
    private SiteUser user;
	
	private Integer height;
	private Integer weight;
	
	@Enumerated(EnumType.STRING) // Use e-num
    private UserGender gender;
	
}
