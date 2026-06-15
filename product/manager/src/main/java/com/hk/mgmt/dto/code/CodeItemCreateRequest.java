package com.hk.mgmt.dto.code;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "코드 항목 등록 요청")
public record CodeItemCreateRequest(

        @Schema(description = "코드 ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String codeId,

        @Schema(description = "코드값")
        @Size(max = 250)
        String codeValue,

        @Schema(description = "카테고리 ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String codeCategory,

        @Schema(description = "순서")
        Integer codeSeq,

        @Schema(description = "상태", allowableValues = {"00", "99"}, defaultValue = "00")
        @Pattern(regexp = "^(00|99)$")
        String status,

        @Schema(description = "참조 문자열 1")
        @Size(max = 100)
        String refString1,

        @Schema(description = "참조 문자열 2")
        @Size(max = 100)
        String refString2,

        @Schema(description = "참조 문자열 3")
        @Size(max = 100)
        String refString3,

        @Schema(description = "참조 숫자 1")
        BigDecimal refNumber1,

        @Schema(description = "참조 숫자 2")
        BigDecimal refNumber2,

        @Schema(description = "참조 숫자 3")
        BigDecimal refNumber3,

        @Schema(description = "비고")
        @Size(max = 255)
        String remark

) {}