package com.hk.mgmt.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank @Size(min = 8, max = 100) String newPassword,
        @NotBlank                           String confirmPassword
) {}