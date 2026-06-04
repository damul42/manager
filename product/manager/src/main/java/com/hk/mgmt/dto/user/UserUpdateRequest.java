package com.hk.mgmt.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 수정 요청 — 변경하지 않을 필드는 null로 전달")
public record UserUpdateRequest(

        @Schema(description = "표시 이름 (null 이면 변경 안 함)", example = "홍길동", maxLength = 100)
        @Size(max = 100)
        String name,

        @Schema(description = "계정 상태 (null 이면 변경 안 함)", allowableValues = {"E", "D"}, example = "E")
        @Pattern(regexp = "^[ED]$", message = "상태는 E(활성) 또는 D(비활성)만 허용됩니다.")
        String status,

        @Schema(description = "새 비밀번호 (null 이면 변경 안 함, 8–100자)", example = "newpass123", minLength = 8, maxLength = 100)
        @Size(min = 8, max = 100, message = "비밀번호는 8자 이상이어야 합니다.")
        String newPassword,

        @Schema(description = "새 비밀번호 확인 — newPassword 와 동일해야 함", example = "newpass123")
        String confirmPassword

) {}