package com.luci.gamification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class GamificationSecurityConfigPassword {

	// define a BCryptPasswordEncoder bean
	// defined in a different class to avoid dependency cycle (since it is used in
	// the GamificationSecurityConfig and UserService)

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
