package com.hk.mgmt.dto.user;

import com.hk.mgmt.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.format.DateTimeFormatter;

@Schema(description = "사용자 목록 항목")
public record UserListDto(

        @Schema(description = "사용자 ID (UUID)")
        String id,

        @Schema(description = "이메일 (로그인 ID)", example = "user@example.com")
        String email,

        @Schema(description = "표시 이름 — 직원 이름 > 사용자 이름 > 이메일 순 우선")
        String displayName,

        @Schema(description = "연동된 직원 사번 (미연동 시 null)", example = "EMP001")
        String employeeNo,

        @Schema(description = "부서 ID (미연동 시 null)", example = "DEPT01")
        String orgId,

        @Schema(description = "계정 상태", allowableValues = {"E", "D"}, example = "E")
        String status,

        @Schema(description = "직원 연동 여부")
        boolean hasEmployee,

        @Schema(description = "계정 생성 일시 (yyyy-MM-dd)")
        String createdAt

) {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static UserListDto from(User user) {
        var emp = user.getEmployee();
        return new UserListDto(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                emp != null ? emp.getEmployeeNo() : null,
                emp != null ? emp.getOrgId()      : null,
                user.getStatus(),
                emp != null,
                user.getCreatedAt() != null ? user.getCreatedAt().format(FMT) : null
        );
    }
}