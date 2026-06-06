package com.ams.core.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
    @NotBlank(message = "Refresh token cannot be empty")
    String refreshToken
) {}
