package com.ams.core.dto;

public record LoginResponse(
    String accessToken,
    String refreshToken,
    String username,
    String role
) {}
