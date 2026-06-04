package com.hk.mgmt.dto.auth;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String userId,
        boolean passwordChangeRequired
) {}