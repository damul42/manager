package com.hk.mgmt.dto.code;

import com.hk.mgmt.domain.Code;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "코드 항목 목록")
public record CodeItemListDto(

        @Schema(description = "ID (UUID)")
        String id,

        @Schema(description = "코드 ID")
        String codeId,

        @Schema(description = "코드값")
        String codeValue,

        @Schema(description = "카테고리 ID")
        String codeCategory,

        @Schema(description = "순서")
        Integer codeSeq,

        @Schema(description = "상태", allowableValues = {"00", "99"})
        String status,

        @Schema(description = "참조 문자열 1")
        String refString1,

        @Schema(description = "참조 문자열 2")
        String refString2,

        @Schema(description = "참조 문자열 3")
        String refString3,

        @Schema(description = "참조 숫자 1")
        BigDecimal refNumber1,

        @Schema(description = "참조 숫자 2")
        BigDecimal refNumber2,

        @Schema(description = "참조 숫자 3")
        BigDecimal refNumber3,

        @Schema(description = "비고")
        String remark

) {
    public static CodeItemListDto from(Code code) {
        return new CodeItemListDto(
                code.getId(),
                code.getCodeId(),
                code.getCodeValue(),
                code.getCodeCategory(),
                code.getCodeSeq(),
                code.getStatus(),
                code.getRefString1(),
                code.getRefString2(),
                code.getRefString3(),
                code.getRefNumber1(),
                code.getRefNumber2(),
                code.getRefNumber3(),
                code.getRemark()
        );
    }
}