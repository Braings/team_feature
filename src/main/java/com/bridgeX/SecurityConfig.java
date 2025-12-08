package com.bridgeX;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				
				// root, sign & login => Everyone 
				.requestMatchers(
						"/", "/index.html",
		                "/favicon.ico",
		                "/assets/**",
		                "/static/**", "/css/**", "/js/**", "/images/**"
		                ).permitAll()
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				.requestMatchers("/api/sign", "/api/sign/**", "/api/login").permitAll()
				.requestMatchers("/test/**", "/h2_BX-console/**").permitAll()

				// forum(reviews) read (GET) is public
				.requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()

				// forum(reviews) write (POST, PUT, DELETE)는 인증 필요
				.requestMatchers(HttpMethod.POST, "/api/reviews/**").authenticated()
				.requestMatchers(HttpMethod.PUT, "/api/reviews/**").authenticated()
				.requestMatchers(HttpMethod.DELETE, "/api/reviews/**").authenticated()
				
				// Exercise Facilities 는 모두 조회 가능
				.requestMatchers("/api/exerciseFacilities/**").permitAll()

				// else => private
				.anyRequest().authenticated()
				)
		// Allow H2-console Frame
		.headers(headers -> headers
				.frameOptions(frame -> frame.sameOrigin())
				);
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
