package com.luci.gamification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.luci.gamification.service.UserService;



@Configuration
@EnableWebSecurity
public class GamificationSecurityConfig {

	// security configuration
	
	@Autowired
	private UserService userService;

	@Autowired
	private GamificationAuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// filter access based on role, handle user authentication and logout
		// allow access to resources
		
		http.authorizeHttpRequests().requestMatchers("/").permitAll().requestMatchers("/confirmAccount").permitAll()
				.requestMatchers("/recovery/*").anonymous()
				.requestMatchers("/register/*").anonymous()
				.requestMatchers("/userdata/*").hasRole("USER")
				.requestMatchers("/deleteConfirm").permitAll()
				.requestMatchers("/admin/*").hasRole("ADMIN")
				.requestMatchers("/account/*").hasRole("USER")
				.requestMatchers("/menu").hasRole("USER")
				.requestMatchers("/quest/*").hasRole("USER")
				.requestMatchers("/leaderboard/*").hasRole("USER")
				.requestMatchers("/profile/*").hasRole("USER")
				.requestMatchers("/user-photos/*").permitAll()
				.requestMatchers("badge-photos/*").permitAll()
				.requestMatchers("/img/*").permitAll().requestMatchers("/css/*").permitAll()
				.requestMatchers("/favicon.ico").permitAll().and().rememberMe().key("uniqueAndSecret")
				.tokenValiditySeconds(86400).rememberMeParameter("remember-me").and().formLogin().loginPage("/login")
				.loginProcessingUrl("/authenticateTheUser").successHandler(authenticationSuccessHandler).permitAll()
				.and().logout().permitAll().deleteCookies("JSESSIONID");

		return http.build();
	}

	// authentication provider which uses the custom UserService and a BCryptPasswordEncoder
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder);
		return auth;
	}

}
