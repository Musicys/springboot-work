package com.springbootinit.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "your-secret-key-your-secret-key-your-secret-key";
    private static final long EXPIRATION_TIME = 86400000;

    public String generateToken(Long userId, Integer userType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userType", userType)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.getSubject());
    }

    public Integer getUserTypeFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userType", Integer.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
