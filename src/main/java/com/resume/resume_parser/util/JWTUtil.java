package com.resume.resume_parser.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

	public JWTUtil() {
		
	}
	
	final  String SECRET = "thisisaverysecurelongsecretkeyand123!@#456";
	final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
	final long EXPIRATIONTIME = 1000*60*60;

	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(KEY, SignatureAlgorithm.HS256)
				.compact();
	}
	
	   public String extractUsername(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(KEY)
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	    }

	    public boolean validateToken(String token, String username) {
	        String extractedUsername = extractUsername(token);
	        return extractedUsername.equals(username) && !isTokenExpired(token);
	    }

	    private boolean isTokenExpired(String token) {
	        Date expiration = Jwts.parserBuilder()
	                .setSigningKey(KEY)
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getExpiration();
	        return expiration.before(new Date());
	    }

}
