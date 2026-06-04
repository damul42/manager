package com.hk.mgmt.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "사용자 역할 수정 요청")
public record UserRolesUpdateRequest(
        @Schema(description = "부여할 역할 ID 목록 (빈 배열이면 전체 해제)") List<String> roleIds
) {}