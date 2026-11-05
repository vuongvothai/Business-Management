package com.businessmanagement.auth.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresInSeconds,
                            Long userId, String username, String fullName, String role) {
}
