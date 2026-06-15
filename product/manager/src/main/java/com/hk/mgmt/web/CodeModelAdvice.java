package com.hk.mgmt.web;

import com.hk.mgmt.service.CodeCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 페이지 컨트롤러 전용 — 모든 Thymeleaf 모델에 'codes' 헬퍼를 자동 주입한다.
 *
 * 템플릿 사용 예:
 *   <option th:each="c : ${codes.get('USER_STATUS')}"
 *           th:value="${c.codeId}" th:text="${c.codeValue}">
 */
@ControllerAdvice(basePackages = "com.hk.mgmt.controller.pages")
@RequiredArgsConstructor
public class CodeModelAdvice {

    private final CodeCacheService codeCacheService;

    @ModelAttribute("codes")
    public CodeHelper codes() {
        return new CodeHelper(codeCacheService);
    }
}