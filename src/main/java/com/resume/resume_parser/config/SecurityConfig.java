package com.resume.resume_parser.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.resume.resume_parser.Filter.JWTFilter;
import com.resume.resume_parser.service.impl.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	
	@Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
	
	
	  @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http.csrf().disable()
	            .cors().and()
	            .formLogin().disable()
	            .authorizeRequests()
	                .antMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
	                .anyRequest().authenticated()
	            .and()
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
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
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Use your deployed domain here, not "*"
        config.setAllowedOrigins(List.of("http://resumeparser-n8f2zihe.b4a.run", "https://resumeparser-n8f2zihe.b4a.run"));

        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


	
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

}
