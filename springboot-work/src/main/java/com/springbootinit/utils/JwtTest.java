package com.springbootinit.utils;

public class JwtTest {
    public static void main(String[] args) {
        JwtUtils jwtUtils = new JwtUtils();
        
        // Generate token
        String token = jwtUtils.generateToken(6L, 3);
        System.out.println("Generated token: " + token);
        
        // Validate token
        boolean valid = jwtUtils.validateToken(token);
        System.out.println("Token valid: " + valid);
        
        // Get user info from token
        try {
            Long userId = jwtUtils.getUserIdFromToken(token);
            Integer userType = jwtUtils.getUserTypeFromToken(token);
            System.out.println("User ID: " + userId);
            System.out.println("User Type: " + userType);
        } catch (Exception e) {
            System.out.println("Error parsing token: " + e.getMessage());
        }
    }
}