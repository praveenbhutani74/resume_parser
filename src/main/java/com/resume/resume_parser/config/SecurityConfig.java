package com.resume.resume_parser.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


import com.resume.resume_parser.service.impl.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	    httpSecurity
	        .csrf(AbstractHttpConfigurer::disable)
	        .cors().and() // Add this line
	        .authorizeRequests(auth -> auth
	            .antMatchers(
	                "/swagger-ui/**",
	                "/v3/api-docs/**",
	                "/swagger-resources/**",
	                "/webjars/**",
	                "/api/auth/**",
	                "/**"  // allow all for now
	            ).permitAll()
	            .anyRequest().permitAll() // or .authenticated() if needed
	        )
	        .formLogin().disable();

	    return httpSecurity.build();
	}

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
    	return http.getSharedObject(AuthenticationManagerBuilder.class)
    			.userDetailsService(customUserDetailsService)
    			.passwordEncoder(passwordEncoder())
    			.and()
    			.build();
    }

    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("*")); 
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

	
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

}
