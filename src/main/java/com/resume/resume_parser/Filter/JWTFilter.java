package com.resume.resume_parser.Filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.resume.resume_parser.dto.UserDTO;
import com.resume.resume_parser.service.impl.CustomUserDetailsService;
import com.resume.resume_parser.util.JWTUtil;

@Component
public class JWTFilter extends OncePerRequestFilter {
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    public JWTFilter() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // IMPORTANT: Skip JWT processing for OPTIONS requests (CORS preflight)
        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // IMPORTANT: Skip JWT processing for auth endpoints
        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/api/auth/") || 
            requestPath.startsWith("/swagger-ui/") || 
            requestPath.startsWith("/v3/api-docs/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Process JWT token for other endpoints
        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
