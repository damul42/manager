package com.hk.mgmt.controller.api;

import com.hk.mgmt.dto.role.RoleCreateRequest;
import com.hk.mgmt.dto.role.RoleDetailDto;
import com.hk.mgmt.dto.role.RoleListDto;
import com.hk.mgmt.dto.role.RoleUpdateRequest;
import com.hk.mgmt.service.RoleService;
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

@Tag(name = "Role", description = "역할 관리 API")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleApiController {

    private final RoleService roleService;

    @Operation(summary = "역할 목록 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleListDto.class))))
    })
    @GetMapping
    public ResponseEntity<List<RoleListDto>> list() {
        return ResponseEntity.ok(roleService.getList());
    }

    @Operation(summary = "역할 상세 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RoleDetailDto.class))),
        @ApiResponse(responseCode = "404", description = "역할 없음", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleDetailDto> detail(
            @Parameter(description = "역할 ID", required = true) @PathVariable String id) {
        return ResponseEntity.ok(roleService.getDetail(id));
    }

    @Operation(summary = "역할 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "등록 성공",
            headers = @Header(name = "Location", description = "/api/roles/{id}"),
            content = @Content(schema = @Schema(implementation = RoleDetailDto.class))),
        @ApiResponse(responseCode = "400", description = "입력값 오류", content = @Content),
        @ApiResponse(responseCode = "409", description = "역할명 중복", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RoleDetailDto> create(@Valid @RequestBody RoleCreateRequest request) {
        RoleDetailDto created = roleService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "역할 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = RoleDetailDto.class))),
        @ApiResponse(responseCode = "400", description = "입력값 오류", content = @Content),
        @ApiResponse(responseCode = "404", description = "역할 없음", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoleDetailDto> update(
            @Parameter(description = "역할 ID", required = true) @PathVariable String id,
            @Valid @RequestBody RoleUpdateRequest request) {
        return ResponseEntity.ok(roleService.update(id, request));
    }

    @Operation(summary = "역할 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "역할 없음", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "역할 ID", required = true) @PathVariable String id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}