package com.hk.mgmt.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("messageSource")
public class JsonMessageSource extends AbstractMessageSource {

    private static final Locale FALLBACK = Locale.KOREAN;
    private static final String BASE_PATH = "static/data/translations/";

    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    public JsonMessageSource(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        setUseCodeAsDefaultMessage(true);
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = findMessage(code, locale);
        return msg != null ? createMessageFormat(msg, locale) : null;
    }

    private String findMessage(String code, Locale locale) {
        String msg = load(locale.getLanguage()).get(code);
        if (msg == null && !locale.getLanguage().equals(FALLBACK.getLanguage())) {
            msg = load(FALLBACK.getLanguage()).get(code);
        }
        return msg;
    }

    /** Thymeleaf 템플릿 외 JS 주입용: 현재 로케일의 전체 메시지 맵 반환 */
    public Map<String, String> getMessages(Locale locale) {
        Map<String, String> result = new java.util.LinkedHashMap<>();
        // fallback 먼저 로드하고, 현재 locale 값으로 덮어씀 (덮어쓰기 우선순위 보장)
        result.putAll(load(FALLBACK.getLanguage()));
        if (!locale.getLanguage().equals(FALLBACK.getLanguage())) {
            result.putAll(load(locale.getLanguage()));
        }
        return Collections.unmodifiableMap(result);
    }

    private Map<String, String> load(String lang) {
        return cache.computeIfAbsent(lang, l -> {
            ClassPathResource res = new ClassPathResource(BASE_PATH + l + ".json");
            if (!res.exists()) {
                res = new ClassPathResource(BASE_PATH + FALLBACK.getLanguage() + ".json");
            }
            try (var in = res.getInputStream()) {
                return objectMapper.readValue(in, new TypeReference<>() {});
            } catch (IOException e) {
                return Map.of();
            }
        });
    }
}