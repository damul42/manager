package com.hk.mgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.time.Duration;
import java.util.Locale;

@Configuration
public class I18nConfig {

    /**
     * 쿠키 기반 로케일 저장.
     * 최초 방문 시 한국어, ?lang=en 파라미터로 변경 가능.
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver("lang");
        resolver.setDefaultLocale(Locale.KOREAN);
        resolver.setCookieMaxAge(Duration.ofDays(365));
        return resolver;
    }

    /** ?lang=en, ?lang=ko 파라미터로 언어 전환 */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }
}