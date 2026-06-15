package com.hk.mgmt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hk.mgmt.dto.code.CodeCategoryListDto;
import com.hk.mgmt.dto.code.CodeItemListDto;
import com.hk.mgmt.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * 공통 코드를 Redis에 캐싱한다.
 * 활성(status='00') 데이터만 캐싱하며, TTL은 60분.
 * 캐시 키:
 *   codes:cat:all          — 활성 카테고리 전체
 *   codes:items:{catId}    — 해당 카테고리의 활성 코드 항목
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeCacheService {

    private static final String   CAT_ALL_KEY    = "codes:cat:all";
    private static final String   ITEMS_PREFIX   = "codes:items:";
    private static final Duration TTL            = Duration.ofMinutes(60);

    private final CodeRepository      codeRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper        objectMapper;

    // ── 조회 ─────────────────────────────────────────────────────────

    /** 활성 카테고리 목록 (캐시 미스 시 DB 조회 후 캐싱) */
    public List<CodeCategoryListDto> getAllActiveCategories() {
        String json = redisTemplate.opsForValue().get(CAT_ALL_KEY);
        if (json != null) {
            try {
                return objectMapper.readValue(json, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                log.warn("Code category cache parse error", e);
            }
        }
        List<CodeCategoryListDto> result = codeRepository.searchCategories(null, "00")
                .stream().map(CodeCategoryListDto::from).toList();
        store(CAT_ALL_KEY, result);
        return result;
    }

    /** 카테고리 ID로 활성 코드 항목 조회 (캐시 미스 시 DB 조회 후 캐싱) */
    public List<CodeItemListDto> getItemsByCategoryId(String categoryId) {
        String key  = ITEMS_PREFIX + categoryId;
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                return objectMapper.readValue(json, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                log.warn("Code items cache parse error, categoryId={}", categoryId, e);
            }
        }
        List<CodeItemListDto> result = codeRepository
                .searchItems(categoryId, null, null, "00")
                .stream().map(CodeItemListDto::from).toList();
        store(key, result);
        return result;
    }

    /**
     * 카테고리값(예: "USER_STATUS")으로 활성 코드 항목 조회.
     * Thymeleaf 템플릿과 서비스 레이어에서 사용.
     */
    public List<CodeItemListDto> getByValue(String categoryValue) {
        return getAllActiveCategories().stream()
                .filter(c -> categoryValue.equals(c.codeValue()))
                .findFirst()
                .map(c -> getItemsByCategoryId(c.id()))
                .orElse(List.of());
    }

    // ── 캐시 무효화 ───────────────────────────────────────────────────

    /** 카테고리 및 해당 항목 캐시 무효화 */
    public void evict(String categoryId) {
        redisTemplate.delete(ITEMS_PREFIX + categoryId);
        redisTemplate.delete(CAT_ALL_KEY);
    }

    /** 카테고리 목록 캐시만 무효화 */
    public void evictCategories() {
        redisTemplate.delete(CAT_ALL_KEY);
    }

    // ── 내부 ──────────────────────────────────────────────────────────

    private void store(String key, Object data) {
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(data), TTL);
        } catch (JsonProcessingException e) {
            log.warn("Failed to cache code data for key={}", key, e);
        }
    }
}