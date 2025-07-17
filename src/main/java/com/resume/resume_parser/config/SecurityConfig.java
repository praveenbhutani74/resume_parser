package com.resume.resume_parser.config;

import java.util.List;
import com.resume.resume_parser.Filter.JWTFilter;
import com.resume.resume_parser.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JWTFilter jwtFilter;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http.csrf().disable()
    //         .cors().and()
    //         .formLogin().disable()
    //         .authorizeRequests()
    //             .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    //             .antMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
    //             .anyRequest().authenticated()
    //         .and()
    //         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
    //     http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    //     return http.build();
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors().and()
            .authorizeRequests()
                .anyRequest().permitAll();  // Allow ALL requests
        
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
    
   @Bean
@Order(Ordered.HIGHEST_PRECEDENCE)
public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    
    // IMPORTANT: Must be false when using "*" for origins
    config.setAllowCredentials(false);
    
    // Allow all origins
    config.addAllowedOrigin("*");
    
    // Allow all headers
    config.addAllowedHeader("*");
    
    // Allow all methods
    config.addAllowedMethod("*");
    
    // Optional: Set max age for preflight cache
    config.setMaxAge(3600L);
    
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
