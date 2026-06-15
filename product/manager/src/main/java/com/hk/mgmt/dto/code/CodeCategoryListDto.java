package com.hk.mgmt.dto.code;

import com.hk.mgmt.domain.Code;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코드 카테고리 목록 항목")
public record CodeCategoryListDto(

        @Schema(description = "ID (UUID)")
        String id,

        @Schema(description = "카테고리 코드값")
        String codeValue,

        @Schema(description = "순서")
        Integer codeSeq,

        @Schema(description = "상태", allowableValues = {"00", "99"})
        String status,

        @Schema(description = "비고")
        String remark

) {
    public static CodeCategoryListDto from(Code code) {
        return new CodeCategoryListDto(
                code.getId(),
                code.getCodeValue(),
                code.getCodeSeq(),
                code.getStatus(),
                code.getRemark()
        );
    }
}