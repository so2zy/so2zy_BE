package com.aroom.global.security;

import com.aroom.global.jwt.service.JwtService;
import com.aroom.global.security.formlogin.CustomFormLoginFilter;
import com.aroom.global.security.formlogin.CustomFormLoginProvider;
import com.aroom.global.security.jwt.JwtAuthenticationFilter;
import com.aroom.global.security.jwt.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtAuthenticationProvider jwtAuthenticationProvider;

	public SecurityConfig(
		UserDetailsService userDetailsService, AuthenticationConfiguration authenticationConfiguration,
		JwtAuthenticationProvider jwtAuthenticationProvider) {
		this.userDetailsService = userDetailsService;
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtAuthenticationProvider = jwtAuthenticationProvider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CustomFormLoginProvider customFormLoginProvider() {
		return new CustomFormLoginProvider(passwordEncoder(), userDetailsService);
	}

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		ProviderManager authenticationManager = (ProviderManager)authenticationConfiguration.getAuthenticationManager();
		authenticationManager.getProviders().add(customFormLoginProvider());
		authenticationManager.getProviders().add(jwtAuthenticationProvider);
		return authenticationManager;
	}

	@Bean
	CustomFormLoginFilter customFormLoginFilter(JwtService jwtService) throws Exception {
		return new CustomFormLoginFilter(authenticationManager(), jwtService);
	}

	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		return new JwtAuthenticationFilter(authenticationManager());
	}
}
