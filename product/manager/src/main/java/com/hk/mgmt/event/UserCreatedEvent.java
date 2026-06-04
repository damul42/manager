package com.hk.mgmt.event;

/**
 * 직원 등록 시 User 자동 생성 후 발행되는 이벤트.
 * 이메일 발송 등 후속 처리는 {@link UserAccountEventListener}에서 구현.
 */
public record UserCreatedEvent(
        String userId,
        String email,
        String name,
        String tempPassword
) {}