package com.hk.mgmt.controller.api;

import com.hk.mgmt.dto.code.*;
import com.hk.mgmt.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.List;

@Tag(name = "Code", description = "공통 코드 관리 API")
@RestController
@RequestMapping("/api/codes")
@RequiredArgsConstructor
public class CodeApiController {

    private final CodeService codeService;

    // ── 카테고리 ──────────────────────────────────────────────────────

    @Operation(summary = "카테고리 목록 조회")
    @GetMapping("/categories")
    public ResponseEntity<List<CodeCategoryListDto>> listCategories(
            @RequestParam(required = false) String value,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(codeService.getCategories(value, status));
    }

    @Operation(summary = "카테고리 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "등록 성공",
            content = @Content(schema = @Schema(implementation = CodeCategoryListDto.class))),
        @ApiResponse(responseCode = "400", description = "입력값 오류", content = @Content)
    })
    @PostMapping("/categories")
    public ResponseEntity<CodeCategoryListDto> createCategory(
            @Valid @RequestBody CodeCategoryCreateRequest request) {
        return ResponseEntity.ok(codeService.createCategory(request));
    }

    @Operation(summary = "카테고리 수정")
    @PutMapping("/categories/{id}")
    public ResponseEntity<CodeCategoryListDto> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CodeCategoryUpdateRequest request) {
        return ResponseEntity.ok(codeService.updateCategory(id, request));
    }

    @Operation(summary = "카테고리 삭제 (비활성화 + 하위 코드 일괄 비활성화)")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        codeService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // ── 코드 항목 ─────────────────────────────────────────────────────

    @Operation(summary = "코드 항목 목록 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CodeItemListDto.class))))
    })
    @GetMapping("/categories/{categoryId}/items")
    public ResponseEntity<List<CodeItemListDto>> listItems(
            @Parameter(description = "카테고리 ID", required = true)
            @PathVariable String categoryId,
            @RequestParam(required = false) String codeId,
            @RequestParam(required = false) String value,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(codeService.getItems(categoryId, codeId, value, status));
    }

    @Operation(summary = "코드 항목 등록")
    @PostMapping("/items")
    public ResponseEntity<CodeItemListDto> createItem(
            @Valid @RequestBody CodeItemCreateRequest request) {
        return ResponseEntity.ok(codeService.createItem(request));
    }

    @Operation(summary = "코드 항목 수정")
    @PutMapping("/items/{id}")
    public ResponseEntity<CodeItemListDto> updateItem(
            @PathVariable String id,
            @Valid @RequestBody CodeItemUpdateRequest request) {
        return ResponseEntity.ok(codeService.updateItem(id, request));
    }

    @Operation(summary = "코드 항목 삭제 (비활성화)")
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        codeService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}