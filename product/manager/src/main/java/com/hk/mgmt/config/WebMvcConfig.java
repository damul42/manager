package com.hk.mgmt.config;

import com.hk.mgmt.web.MenuAccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final MenuAccessInterceptor menuAccessInterceptor;
    private final LocaleChangeInterceptor localeChangeInterceptor;

    /**
     * 인터셉터 등록
     *
     * 제외 경로:
     *   /api/**          — REST API (SecurityFilterChain이 인가 담당)
     *   /auth/**         — 로그인·회원가입 페이지
     *   /auth-card/**
     *   /auth-split/**
     *   /error/**        — Spring 에러 핸들링
     *   /swagger-ui/**   — Swagger UI
     *   /api-docs/**     — OpenAPI 명세
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로케일 변경은 메뉴 접근 체크보다 먼저 실행되어야 한다
        registry.addInterceptor(localeChangeInterceptor);

        registry.addInterceptor(menuAccessInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/**",
                        "/login",
                        "/change-password",
                        "/dashboard/**",
                        "/auth/**",
                        "/auth-card/**",
                        "/auth-split/**",
                        "/error/**",
                        "/swagger-ui/**",
                        "/api-docs/**"
                );
    }
}