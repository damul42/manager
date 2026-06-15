package com.hk.mgmt.dto.auth;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken
) {}