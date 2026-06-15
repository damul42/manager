package com.hk.mgmt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tbl_sys_code", schema = "manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {

    @Id
    @Column(length = 45)
    private String id;

    @Column(nullable = false, length = 100)
    private String codeId;

    @Column(nullable = false, length = 5)
    private String codeType; // 'G' = Category, 'C' = Item

    private Integer codeSeq;

    @Column(length = 250)
    private String codeValue;

    @Column(nullable = false, length = 45)
    private String codeCategory;

    @Column(nullable = false, length = 5)
    private String status; // '00' = ACTIVE, '99' = INACTIVE

    @Column(length = 100)
    private String refString1;

    @Column(length = 100)
    private String refString2;

    @Column(length = 100)
    private String refString3;

    @Column(precision = 13, scale = 4)
    private BigDecimal refNumber1;

    @Column(precision = 13, scale = 4)
    private BigDecimal refNumber2;

    @Column(precision = 13, scale = 4)
    private BigDecimal refNumber3;

    @Column(length = 255)
    private String remark;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            String newId = UUID.randomUUID().toString();
            this.id = newId;
            if ("G".equals(this.codeType)) {
                this.codeId = newId;
                this.codeCategory = newId;
            }
        }
    }

    @Builder
    private Code(String codeId, String codeType, Integer codeSeq, String codeValue,
                 String codeCategory, String status,
                 String refString1, String refString2, String refString3,
                 BigDecimal refNumber1, BigDecimal refNumber2, BigDecimal refNumber3,
                 String remark) {
        this.codeId = codeId;
        this.codeType = codeType;
        this.codeSeq = codeSeq;
        this.codeValue = codeValue;
        this.codeCategory = codeCategory;
        this.status = status != null ? status : "00";
        this.refString1 = refString1;
        this.refString2 = refString2;
        this.refString3 = refString3;
        this.refNumber1 = refNumber1;
        this.refNumber2 = refNumber2;
        this.refNumber3 = refNumber3;
        this.remark = remark;
    }

    // ── 팩토리 메서드 ──────────────────────────────────────────────────

    public static Code createCategory(String codeValue, Integer codeSeq, String status, String remark) {
        return Code.builder()
                .codeType("G")
                .codeValue(codeValue)
                .codeSeq(codeSeq)
                .status(status)
                .remark(remark)
                .build();
    }

    public static Code createItem(String codeId, String codeValue, String codeCategory,
                                   Integer codeSeq, String status,
                                   String refString1, String refString2, String refString3,
                                   BigDecimal refNumber1, BigDecimal refNumber2, BigDecimal refNumber3,
                                   String remark) {
        return Code.builder()
                .codeId(codeId)
                .codeType("C")
                .codeValue(codeValue)
                .codeCategory(codeCategory)
                .codeSeq(codeSeq)
                .status(status)
                .refString1(refString1)
                .refString2(refString2)
                .refString3(refString3)
                .refNumber1(refNumber1)
                .refNumber2(refNumber2)
                .refNumber3(refNumber3)
                .remark(remark)
                .build();
    }

    // ── 수정 ──────────────────────────────────────────────────────────

    public void updateCategory(String codeValue, Integer codeSeq, String status, String remark) {
        this.codeValue = codeValue;
        this.codeSeq = codeSeq;
        this.status = status;
        this.remark = remark;
    }

    public void updateItem(String codeId, String codeValue, Integer codeSeq, String status,
                            String refString1, String refString2, String refString3,
                            BigDecimal refNumber1, BigDecimal refNumber2, BigDecimal refNumber3,
                            String remark) {
        this.codeId = codeId;
        this.codeValue = codeValue;
        this.codeSeq = codeSeq;
        this.status = status;
        this.refString1 = refString1;
        this.refString2 = refString2;
        this.refString3 = refString3;
        this.refNumber1 = refNumber1;
        this.refNumber2 = refNumber2;
        this.refNumber3 = refNumber3;
        this.remark = remark;
    }

    public void changeStatus(String status) {
        this.status = status;
    }
}