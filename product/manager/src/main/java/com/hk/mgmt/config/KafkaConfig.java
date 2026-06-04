package com.hk.mgmt.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaConfig {

    // ── 토픽 정의 ──────────────────────────────────────────────────────
    // 애플리케이션 기동 시 토픽이 없으면 자동 생성된다.

    @Bean
    public NewTopic userEventTopic() {
        return TopicBuilder.name("user.events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    // ── 에러 핸들러 ────────────────────────────────────────────────────
    // Spring Boot가 이 빈을 자동으로 ConcurrentKafkaListenerContainerFactory에 주입한다.

    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        // 1s → 2s → 4s 지수 백오프, 최대 3회 재시도 후 처리 중단
        ExponentialBackOff backOff = new ExponentialBackOff(1_000L, 2.0);
        backOff.setMaxAttempts(3);
        return new DefaultErrorHandler(backOff);
    }
}