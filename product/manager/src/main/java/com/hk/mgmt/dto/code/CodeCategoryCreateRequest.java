package com.hk.mgmt.dto.code;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "코드 카테고리 등록 요청")
public record CodeCategoryCreateRequest(

        @Schema(description = "카테고리 코드값", example = "USER_STATUS", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 250)
        String codeValue,

        @Schema(description = "순서")
        Integer codeSeq,

        @Schema(description = "상태", allowableValues = {"00", "99"}, defaultValue = "00")
        @Pattern(regexp = "^(00|99)$")
        String status,

        @Schema(description = "비고")
        @Size(max = 255)
        String remark

) {}