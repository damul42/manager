package com.hk.mgmt.controller.api;

import com.hk.mgmt.dto.role.RoleListDto;
import com.hk.mgmt.dto.user.UserCreateRequest;
import com.hk.mgmt.dto.user.UserDetailDto;
import com.hk.mgmt.dto.user.UserListDto;
import com.hk.mgmt.dto.user.UserRolesUpdateRequest;
import com.hk.mgmt.dto.user.UserUpdateRequest;
import com.hk.mgmt.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "User", description = "사용자 관리 API — 계정 등록·조회·수정·삭제")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    // ── 등록 ──────────────────────────────────────────────────────────

    @Operation(
        summary = "사용자 등록",
        description = "새 사용자 계정을 생성합니다. 직원 ID를 전달하면 직원 정보와 연동됩니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "등록 성공 — Location 헤더에 생성된 리소스 URI 포함",
            headers = @Header(name = "Location", description = "/api/users/{id}"),
            content = @Content(schema = @Schema(implementation = UserDetailDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "입력값 오류 (이메일 형식, 비밀번호 길이, 불일치 등)", content = @Content),
        @ApiResponse(responseCode = "409", description = "이메일 중복", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserDetailDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "사용자 등록 정보",
                required = true
            )
            @Valid @RequestBody UserCreateRequest request) {

        UserDetailDto created = userService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // ── 목록 조회 ──────────────────────────────────────────────────────

    @Operation(
        summary = "사용자 목록 조회",
        description = "조건에 맞는 사용자 목록을 반환합니다. 파라미터를 모두 생략하면 전체 목록을 반환합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserListDto.class)))
        )
    })
    @GetMapping
    public ResponseEntity<List<UserListDto>> list(
            @Parameter(description = "이메일 (부분 일치)")
            @RequestParam(required = false) String email,

            @Parameter(description = "이름 (부분 일치)")
            @RequestParam(required = false) String name,

            @Parameter(description = "상태 필터", schema = @Schema(allowableValues = {"E", "D"}))
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(userService.getList(email, name, status));
    }

    // ── 상세 조회 ──────────────────────────────────────────────────────

    @Operation(
        summary = "사용자 상세 조회",
        description = "사용자 ID로 계정 정보 및 연동된 직원 정보를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserDetailDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "사용자 없음", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDto> detail(
            @Parameter(description = "사용자 ID (UUID)", required = true, example = "a1b2c3d4-...")
            @PathVariable String id) {

        return ResponseEntity.ok(userService.getDetail(id));
    }

    // ── 수정 ──────────────────────────────────────────────────────────

    @Operation(
        summary = "사용자 수정",
        description = "이름, 상태를 변경하거나 비밀번호를 재설정합니다. `newPassword`를 생략하면 비밀번호는 변경되지 않습니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content(schema = @Schema(implementation = UserDetailDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "입력값 오류 (비밀번호 불일치 등)", content = @Content),
        @ApiResponse(responseCode = "404", description = "사용자 없음", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailDto> update(
            @Parameter(description = "사용자 ID (UUID)", required = true)
            @PathVariable String id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "수정할 정보. 변경하지 않을 필드는 null로 전달합니다.",
                required = true
            )
            @Valid @RequestBody UserUpdateRequest request) {

        return ResponseEntity.ok(userService.update(id, request));
    }

    // ── 역할 조회 ──────────────────────────────────────────────────────

    @Operation(summary = "사용자 역할 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleListDto.class)))),
        @ApiResponse(responseCode = "404", description = "사용자 없음", content = @Content)
    })
    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleListDto>> getRoles(
            @Parameter(description = "사용자 ID", required = true) @PathVariable String id) {
        return ResponseEntity.ok(userService.getUserRoles(id));
    }

    // ── 역할 수정 ──────────────────────────────────────────────────────

    @Operation(summary = "사용자 역할 수정", description = "전달한 역할 ID 목록으로 교체합니다. 빈 배열이면 전체 해제됩니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleListDto.class)))),
        @ApiResponse(responseCode = "404", description = "사용자 없음", content = @Content)
    })
    @PutMapping("/{id}/roles")
    public ResponseEntity<List<RoleListDto>> updateRoles(
            @Parameter(description = "사용자 ID", required = true) @PathVariable String id,
            @RequestBody UserRolesUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUserRoles(id, request.roleIds()));
    }

    // ── 삭제 ──────────────────────────────────────────────────────────

    @Operation(
        summary = "사용자 삭제 (비활성화)",
        description = "사용자를 물리 삭제하지 않고 상태를 `D`(비활성)로 변경합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공 (No Content)"),
        @ApiResponse(responseCode = "404", description = "사용자 없음", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "사용자 ID (UUID)", required = true)
            @PathVariable String id) {

        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}