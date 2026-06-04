package com.hk.mgmt.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserAccountEventListener {

    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        // TODO: 이메일 발송 구현 (JavaMailSender / 외부 메일 서비스)
        // 발송 내용: 계정 생성 안내 + 임시 비밀번호 + 변경 안내
        log.info("[계정 생성] email={} | 임시 비밀번호 발송 예정 (미구현)", event.email());
    }
}