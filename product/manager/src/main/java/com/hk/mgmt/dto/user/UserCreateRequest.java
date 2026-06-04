package com.hk.mgmt.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 등록 요청")
public record UserCreateRequest(

        @Schema(description = "이메일 (로그인 ID, 고유값)", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Email
        String email,

        @Schema(description = "표시 이름 — 직원 연동 시 직원 이름이 우선 사용됨", example = "홍길동")
        String name,

        @Schema(description = "비밀번호 (8자 이상)", example = "password123", minLength = 8, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 8)
        String password,

        @Schema(description = "비밀번호 확인 — password 와 동일해야 함", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String confirmPassword,

        @Schema(description = "계정 상태", allowableValues = {"E", "D"}, defaultValue = "E", example = "E")
        @Pattern(regexp = "^[ED]$")
        String status,

        @Schema(description = "연동할 직원 ID (UUID) — 생략 시 직원 미연동", example = "a1b2c3d4-e5f6-...")
        String employeeId

) {}