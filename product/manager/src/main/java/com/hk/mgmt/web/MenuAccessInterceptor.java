package com.hk.mgmt.web;

import com.hk.mgmt.dto.menu.MenuPermission;
import com.hk.mgmt.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuAccessInterceptor implements HandlerInterceptor {

    private final MenuService menuService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 정적 리소스 등 컨트롤러가 아닌 핸들러는 통과
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 미인증 요청은 SecurityFilterChain이 처리하므로 통과
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return true;
        }

        String requestUrl = request.getRequestURI();
        String email = auth.getName();

        // 역할/메뉴 설정이 아직 없는 사용자는 통과 (초기 구성 허용)
        if (!menuService.hasAccessibleUrls(email)) {
            return true;
        }

        if (!menuService.isAccessible(email, requestUrl)) {
            log.warn("Access denied (no menu): {} → {}", email, requestUrl);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        // URL 패턴으로 필요한 action을 판단하고 메뉴 권한과 대조
        var permOpt = menuService.getPermissionForUrl(email, requestUrl);
        if (permOpt.isPresent() && !hasActionPermission(permOpt.get(), requestUrl)) {
            log.warn("Access denied (insufficient permission): {} → {}", email, requestUrl);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    /**
     * URL 패턴으로 필요한 action을 판단한다.
     * /list  → list 권한 필요
     * /create, /edit → write 권한 필요
     * /{id}  → read 권한 필요
     */
    private boolean hasActionPermission(MenuPermission perm, String url) {
        if (perm.admin()) return true;
        if (url.endsWith("/list"))   return perm.list();
        if (url.endsWith("/create")) return perm.write();
        if (url.endsWith("/edit"))   return perm.write();
        return perm.read();
    }
}