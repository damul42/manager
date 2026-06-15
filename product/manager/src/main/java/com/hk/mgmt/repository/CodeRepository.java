package com.hk.mgmt.repository;

import com.hk.mgmt.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, String> {

    List<Code> findAllByCodeTypeAndCodeCategoryOrderByCodeSeqAsc(String codeType, String codeCategory);

    // ── 중복 체크 ─────────────────────────────────────────────────────

    // 카테고리 codeValue 중복 (등록용)
    boolean existsByCodeTypeAndCodeValue(String codeType, String codeValue);
    // 카테고리 codeValue 중복 (수정용 — 자신 제외)
    boolean existsByCodeTypeAndCodeValueAndIdNot(String codeType, String codeValue, String id);

    // 항목 codeId 중복 (등록용)
    boolean existsByCodeTypeAndCodeCategoryAndCodeId(String codeType, String codeCategory, String codeId);
    // 항목 codeId 중복 (수정용 — 자신 제외)
    boolean existsByCodeTypeAndCodeCategoryAndCodeIdAndIdNot(String codeType, String codeCategory, String codeId, String id);

    // 항목 codeValue 중복 (등록용)
    boolean existsByCodeTypeAndCodeCategoryAndCodeValue(String codeType, String codeCategory, String codeValue);
    // 항목 codeValue 중복 (수정용 — 자신 제외)
    boolean existsByCodeTypeAndCodeCategoryAndCodeValueAndIdNot(String codeType, String codeCategory, String codeValue, String id);

    @Query("""
            SELECT c FROM Code c
            WHERE c.codeType = 'G'
              AND (:value  IS NULL OR LOWER(c.codeValue) LIKE LOWER(CONCAT('%', CAST(:value AS String), '%')))
              AND (:status IS NULL OR c.status = CAST(:status AS String))
            ORDER BY c.codeSeq ASC, c.codeValue ASC
            """)
    List<Code> searchCategories(@Param("value")  String value,
                                @Param("status") String status);

    @Query("""
            SELECT c FROM Code c
            WHERE c.codeType = 'C'
              AND c.codeCategory = CAST(:categoryId AS String)
              AND (:codeId IS NULL OR LOWER(c.codeId)    LIKE LOWER(CONCAT('%', CAST(:codeId AS String), '%')))
              AND (:value  IS NULL OR LOWER(c.codeValue) LIKE LOWER(CONCAT('%', CAST(:value  AS String), '%')))
              AND (:status IS NULL OR c.status = CAST(:status AS String))
            ORDER BY c.codeSeq ASC, c.codeId ASC
            """)
    List<Code> searchItems(@Param("categoryId") String categoryId,
                           @Param("codeId")     String codeId,
                           @Param("value")      String value,
                           @Param("status")     String status);
}