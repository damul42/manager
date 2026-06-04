package com.hk.mgmt.web;

import com.hk.mgmt.config.JsonMessageSource;
import com.hk.mgmt.dto.menu.ModuleMenuDto;
import com.hk.mgmt.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final MenuService menuService;
    private final JsonMessageSource jsonMessageSource;

    /**
     * 모든 Thymeleaf 뷰에 사이드바 메뉴를 자동 주입한다.
     * REST API 경로(/api/**)는 응답이 JSON이므로 빈 목록을 반환해 DB/Redis 조회를 건너뛴다.
     */
    @ModelAttribute("sidenavModules")
    public List<ModuleMenuDto> sidenavModules(HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/api/")) {
            return List.of();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return List.of();
        }

        try {
            return menuService.getMenu(auth.getName());
        } catch (Exception e) {
            log.warn("Failed to load sidenav menu for {}: {}", auth.getName(), e.getMessage());
            return List.of();
        }
    }

    /**
     * 현재 요청 URI를 뷰에 전달해 사이드바 활성 메뉴 강조에 활용한다.
     */
    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * 현재 로케일의 전체 메시지 맵을 뷰에 주입한다.
     * Thymeleaf 인라인 JS에서 {@code const MSG = }로 사용한다.
     */
    @ModelAttribute("messages")
    public Map<String, String> messages(HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/api/")) {
            return Map.of();
        }
        return jsonMessageSource.getMessages(LocaleContextHolder.getLocale());
    }
}