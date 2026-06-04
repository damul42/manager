package com.hk.mgmt.dto.user;

import com.hk.mgmt.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "사용자 상세 정보")
public record UserDetailDto(

        // ── 계정 정보 ─────────────────────────────────────────────────

        @Schema(description = "사용자 ID (UUID)")
        String id,

        @Schema(description = "이메일 (로그인 ID)", example = "user@example.com")
        String email,

        @Schema(description = "표시 이름 — 직원 이름 > 사용자 이름 > 이메일 순 우선")
        String displayName,

        @Schema(description = "사용자 자체 이름 (직원 미연동 시 표시 이름으로 사용)", example = "홍길동")
        String name,

        @Schema(description = "계정 상태", allowableValues = {"E", "D"}, example = "E")
        String status,

        @Schema(description = "계정 생성 일시")
        LocalDateTime createdAt,

        // ── 연동 직원 정보 (직원 미연동 시 모든 필드 null) ──────────────

        @Schema(description = "연동된 직원 ID (UUID) — null 이면 미연동")
        String employeeId,

        @Schema(description = "직원 사번", example = "EMP001")
        String employeeNo,

        @Schema(description = "직원 이름", example = "홍길동")
        String employeeName,

        @Schema(description = "부서 ID", example = "DEPT01")
        String orgId,

        @Schema(description = "직위", example = "대리")
        String position,

        @Schema(description = "휴대폰", example = "010-1234-5678")
        String mobile,

        @Schema(description = "직통 전화", example = "02-1234-5678")
        String phone,

        @Schema(description = "업무 이메일", example = "emp@company.com")
        String employeeEmail,

        @Schema(description = "직원 사진 URL")
        String photo,

        @Schema(description = "입사일 (yyyyMMdd)", example = "20200101")
        String startDate,

        @Schema(description = "퇴사일 (yyyyMMdd) — null 이면 재직 중", example = "20231231")
        String endDate,

        @Schema(description = "직원 재직 상태", allowableValues = {"E", "D"}, example = "E")
        String employeeStatus,

        @Schema(description = "최초 로그인 시 비밀번호 변경 필요 여부")
        boolean passwordChangeRequired

) {
    public static UserDetailDto from(User user) {
        var emp = user.getEmployee();
        return new UserDetailDto(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getName(),
                user.getStatus(),
                user.getCreatedAt(),
                emp != null ? emp.getId()          : null,
                emp != null ? emp.getEmployeeNo()  : null,
                emp != null ? emp.getName()        : null,
                emp != null ? emp.getOrgId()       : null,
                emp != null ? emp.getPosition()    : null,
                emp != null ? emp.getMobile()      : null,
                emp != null ? emp.getPhone()       : null,
                emp != null ? emp.getEmail()       : null,
                emp != null ? emp.getPhoto()       : null,
                emp != null ? emp.getStartDate()   : null,
                emp != null ? emp.getEndDate()     : null,
                emp != null ? emp.getStatus()      : null,
                user.isPasswordChangeRequired()
        );
    }
}