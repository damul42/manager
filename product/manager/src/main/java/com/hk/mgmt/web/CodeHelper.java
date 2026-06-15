package com.hk.mgmt.web;

import com.hk.mgmt.dto.code.CodeItemListDto;
import com.hk.mgmt.service.CodeCacheService;

import java.util.List;

/**
 * Thymeleaf 템플릿에서 ${codes.get('USER_STATUS')} 형태로 코드를 조회할 때 사용.
 * CodeModelAdvice가 모든 페이지 모델에 'codes' 이름으로 주입한다.
 */
public class CodeHelper {

    private final CodeCacheService service;

    public CodeHelper(CodeCacheService service) {
        this.service = service;
    }

    /** 카테고리값으로 활성 코드 항목 목록 반환 */
    public List<CodeItemListDto> get(String categoryValue) {
        return service.getByValue(categoryValue);
    }
}