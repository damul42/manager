package com.hk.mgmt.service;

import com.hk.mgmt.domain.Code;
import com.hk.mgmt.dto.code.*;
import com.hk.mgmt.repository.CodeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeService {

    private final CodeRepository  codeRepository;
    private final CodeCacheService codeCacheService;

    // ── 카테고리 ──────────────────────────────────────────────────────

    public List<CodeCategoryListDto> getCategories(String value, String status) {
        String valueParam  = StringUtils.hasText(value)  ? value  : null;
        String statusParam = StringUtils.hasText(status) ? status : null;
        return codeRepository.searchCategories(valueParam, statusParam)
                .stream().map(CodeCategoryListDto::from).toList();
    }

    @Transactional
    public CodeCategoryListDto createCategory(CodeCategoryCreateRequest req) {
        if (codeRepository.existsByCodeTypeAndCodeValue("G", req.codeValue())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 코드값입니다: " + req.codeValue());
        }
        String status = StringUtils.hasText(req.status()) ? req.status() : "00";
        Code code = Code.createCategory(req.codeValue(), req.codeSeq(), status, req.remark());
        CodeCategoryListDto result = CodeCategoryListDto.from(codeRepository.save(code));
        codeCacheService.evictCategories();
        return result;
    }

    @Transactional
    public CodeCategoryListDto updateCategory(String id, CodeCategoryUpdateRequest req) {
        Code code = findCategory(id);
        if (codeRepository.existsByCodeTypeAndCodeValueAndIdNot("G", req.codeValue(), id)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 코드값입니다: " + req.codeValue());
        }
        String status = StringUtils.hasText(req.status()) ? req.status() : "00";
        code.updateCategory(req.codeValue(), req.codeSeq(), status, req.remark());
        codeCacheService.evict(id);
        return CodeCategoryListDto.from(code);
    }

    @Transactional
    public void deleteCategory(String id) {
        Code category = findCategory(id);
        category.changeStatus("99");
        codeRepository.findAllByCodeTypeAndCodeCategoryOrderByCodeSeqAsc("C", id)
                .forEach(item -> item.changeStatus("99"));
        codeCacheService.evict(id);
    }

    // ── 코드 항목 ─────────────────────────────────────────────────────

    public List<CodeItemListDto> getItems(String categoryId, String codeId, String value, String status) {
        String codeIdParam = StringUtils.hasText(codeId) ? codeId : null;
        String valueParam  = StringUtils.hasText(value)  ? value  : null;
        String statusParam = StringUtils.hasText(status) ? status : null;
        return codeRepository.searchItems(categoryId, codeIdParam, valueParam, statusParam)
                .stream().map(CodeItemListDto::from).toList();
    }

    @Transactional
    public CodeItemListDto createItem(CodeItemCreateRequest req) {
        findCategory(req.codeCategory());
        checkItemDuplicate(req.codeCategory(), req.codeId(), req.codeValue(), null);
        String status = StringUtils.hasText(req.status()) ? req.status() : "00";
        Code code = Code.createItem(
                req.codeId(), req.codeValue(), req.codeCategory(),
                req.codeSeq(), status,
                req.refString1(), req.refString2(), req.refString3(),
                req.refNumber1(), req.refNumber2(), req.refNumber3(),
                req.remark()
        );
        CodeItemListDto result = CodeItemListDto.from(codeRepository.save(code));
        codeCacheService.evict(req.codeCategory());
        return result;
    }

    @Transactional
    public CodeItemListDto updateItem(String id, CodeItemUpdateRequest req) {
        Code code = findItem(id);
        String categoryId = code.getCodeCategory();
        checkItemDuplicate(categoryId, req.codeId(), req.codeValue(), id);
        String status = StringUtils.hasText(req.status()) ? req.status() : "00";
        code.updateItem(
                req.codeId(), req.codeValue(), req.codeSeq(), status,
                req.refString1(), req.refString2(), req.refString3(),
                req.refNumber1(), req.refNumber2(), req.refNumber3(),
                req.remark()
        );
        codeCacheService.evict(categoryId);
        return CodeItemListDto.from(code);
    }

    @Transactional
    public void deleteItem(String id) {
        Code code = findItem(id);
        String categoryId = code.getCodeCategory();
        code.changeStatus("99");
        codeCacheService.evict(categoryId);
    }

    // ── 내부 ──────────────────────────────────────────────────────────

    /** excludeId=null 이면 등록용, 값이 있으면 수정용(자신 제외) */
    private void checkItemDuplicate(String categoryId, String codeId, String codeValue, String excludeId) {
        boolean codeIdDup = excludeId == null
                ? codeRepository.existsByCodeTypeAndCodeCategoryAndCodeId("C", categoryId, codeId)
                : codeRepository.existsByCodeTypeAndCodeCategoryAndCodeIdAndIdNot("C", categoryId, codeId, excludeId);
        if (codeIdDup) {
            throw new IllegalArgumentException("동일 카테고리 내 이미 존재하는 코드 ID입니다: " + codeId);
        }

        if (StringUtils.hasText(codeValue)) {
            boolean codeValueDup = excludeId == null
                    ? codeRepository.existsByCodeTypeAndCodeCategoryAndCodeValue("C", categoryId, codeValue)
                    : codeRepository.existsByCodeTypeAndCodeCategoryAndCodeValueAndIdNot("C", categoryId, codeValue, excludeId);
            if (codeValueDup) {
                throw new IllegalArgumentException("동일 카테고리 내 이미 존재하는 코드값입니다: " + codeValue);
            }
        }
    }

    private Code findCategory(String id) {
        Code code = codeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Code category not found: " + id));
        if (!"G".equals(code.getCodeType())) {
            throw new IllegalArgumentException("Not a category: " + id);
        }
        return code;
    }

    private Code findItem(String id) {
        Code code = codeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Code item not found: " + id));
        if (!"C".equals(code.getCodeType())) {
            throw new IllegalArgumentException("Not a code item: " + id);
        }
        return code;
    }
}