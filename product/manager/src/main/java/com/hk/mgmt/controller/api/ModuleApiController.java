package com.hk.mgmt.controller.api;

import com.hk.mgmt.dto.module.ModuleCreateRequest;
import com.hk.mgmt.dto.module.ModuleDetailDto;
import com.hk.mgmt.dto.module.ModuleListDto;
import com.hk.mgmt.dto.module.ModuleUpdateRequest;
import com.hk.mgmt.dto.module.ModuleWithMenusDto;
import com.hk.mgmt.service.ModuleService;
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

@Tag(name = "Module", description = "모듈 관리 API")
@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleApiController {

    private final ModuleService moduleService;

    // ── 권한 설정용 트리 조회 ─────────────────────────────────────────

    @Operation(summary = "활성 모듈·메뉴 트리 조회", description = "권한 설정 화면에서 사용하는 활성 모듈과 활성 메뉴 목록을 반환합니다.")
    @GetMapping("/with-menus")
    public ResponseEntity<List<ModuleWithMenusDto>> withMenus() {
        return ResponseEntity.ok(moduleService.getModulesWithMenus());
    }

    // ── 등록 ──────────────────────────────────────────────────────────

    @Operation(summary = "모듈 등록", description = "새 모듈과 메뉴를 생성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "등록 성공",
            headers = @Header(name = "Location", description = "/api/modules/{id}"),
            content = @Content(schema = @Schema(implementation = ModuleDetailDto.class))),
        @ApiResponse(responseCode = "400", description = "입력값 오류", content = @Content),
        @ApiResponse(responseCode = "409", description = "모듈명 중복", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ModuleDetailDto> create(
            @Valid @RequestBody ModuleCreateRequest request) {

        ModuleDetailDto created = moduleService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // ── 수정 ──────────────────────────────────────────────────────────

    @Operation(summary = "모듈 수정", description = "모듈 정보와 메뉴 목록을 수정합니다. 메뉴는 전체 교체됩니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = ModuleDetailDto.class))),
        @ApiResponse(responseCode = "400", description = "입력값 오류", content = @Content),
        @ApiResponse(responseCode = "404", description = "모듈 없음", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ModuleDetailDto> update(
            @Parameter(description = "모듈 ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody ModuleUpdateRequest request) {

        return ResponseEntity.ok(moduleService.update(id, request));
    }

    // ── 목록 조회 ──────────────────────────────────────────────────────

    @Operation(summary = "모듈 목록 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ModuleListDto.class))))
    })
    @GetMapping
    public ResponseEntity<List<ModuleListDto>> list(
            @Parameter(description = "모듈명 (부분 일치)")
            @RequestParam(required = false) String name,

            @Parameter(description = "상태 필터", schema = @Schema(allowableValues = {"E", "D"}))
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(moduleService.getList(name, status));
    }

    // ── 상세 조회 ──────────────────────────────────────────────────────

    @Operation(summary = "모듈 상세 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = ModuleDetailDto.class))),
        @ApiResponse(responseCode = "404", description = "모듈 없음", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ModuleDetailDto> detail(
            @Parameter(description = "모듈 ID", required = true)
            @PathVariable String id) {

        return ResponseEntity.ok(moduleService.getDetail(id));
    }

    // ── 삭제 ──────────────────────────────────────────────────────────

    @Operation(summary = "모듈 삭제 (비활성화)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "모듈 없음", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "모듈 ID", required = true)
            @PathVariable String id) {

        moduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}