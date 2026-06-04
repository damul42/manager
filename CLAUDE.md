# Works Manager — CLAUDE.md

## 프로젝트 개요
Spring Boot 4.0.6 / Java 21 기반 HR 관리 시스템.
Thymeleaf + thymeleaf-layout-dialect, Spring Security, JPA (Hibernate), Redis, JWT 인증.

---

## 기술 스택 및 주의사항

### Jackson 3.x (Spring Boot 4.x)
- 패키지: `tools.jackson` (3.x 기준 — `com.fasterxml` 아님)
- `application.yml`에서 `spring.jackson.serialization.write-dates-as-timestamps: false` **사용 불가** — enum 바인딩 오류 발생
- 날짜 직렬화는 DTO `from()` 메서드에서 `DateTimeFormatter`로 직접 포맷

### JWT 인증 (Stateless)
- 페이지 이동: `auth_token` 쿠키 → `JwtAuthenticationFilter`가 쿠키 fallback으로 읽음
- API 호출: `Authorization: Bearer <token>` 헤더 → axios interceptor가 `localStorage.auth_token`으로 설정
- `footer-scripts.html`에서 axios 전역 설정 (헤더 주입 + 401 리다이렉트)

### Thymeleaf Layout Dialect
- `content` fragment → `scripts` fragment 순으로 렌더링
- `footer-scripts`는 `content` 안에 포함 → axios 헤더 설정이 먼저 실행됨
- **절대 중복 axios 로드 금지**: scripts fragment에서 `axios.min.js`를 다시 로드하면 auth 헤더가 초기화됨

### JPA
- `open-in-view: false` → 서비스 메서드 종료 후 JPA 세션 닫힘, lazy loading은 트랜잭션 안에서 처리
- N+1 방지: `LEFT JOIN FETCH` 사용 (`LEFT JOIN`만으로는 안 됨)
- `ddl-auto: validate` → 스키마 변경 시 반드시 수동 마이그레이션 먼저 실행

---

## 인증 플로우

### 로그인
1. `POST /api/auth/login` → `LoginResponse { accessToken, refreshToken, userId, passwordChangeRequired }`
2. 프론트: `auth_token`, `user_id`, `password_change_required` → localStorage + 쿠키 저장
3. `passwordChangeRequired === true` → `/change-password` 리다이렉트

### 비밀번호 변경 강제 적용
- `footer-scripts.html` IIFE: `password_change_required === 'true'`이면 `/change-password`로 강제 이동
- 예외 경로: `/change-password`, `/login`

### 비밀번호 변경
- `PUT /api/auth/change-password` + `ChangePasswordRequest { newPassword, confirmPassword }`
- `Principal`로 현재 사용자 식별
- 성공 시: `passwordChangeRequired = false`, localStorage `password_change_required = 'false'`

---

## Employee → User 자동 생성

Employee 등록 시 email이 있으면 User 자동 생성 (`EmployeeService.create()`):
1. `userRepository.findByEmail(email).isEmpty()` 확인 후 중복 방지
2. `SecureRandom`으로 12자 임시 비밀번호 생성 (혼동 문자 0, O, 1, I, l 제외)
3. `User.createWithTempPassword()` → `passwordChangeRequired = true`
4. Default Role (`c6794018-1239-4bfb-9017-2796bd802817`) → `user_role` 등록
5. `UserCreatedEvent` 발행 → `UserAccountEventListener` (이메일 발송 TODO)

---

## 주요 파일 위치

```
product/manager/src/main/java/com/hk/mgmt/
├── config/
│   ├── SecurityConfig.java
│   └── WebMvcConfig.java          # 인터셉터 제외 경로: /login, /change-password 포함
├── controller/
│   ├── api/AuthApiController.java  # POST /login, PUT /change-password, POST /logout
│   └── pages/AuthPageController.java
├── domain/
│   └── User.java                  # passwordChangeRequired 필드, createWithTempPassword()
├── dto/
│   ├── auth/LoginResponse.java    # accessToken, userId, passwordChangeRequired
│   ├── auth/ChangePasswordRequest.java
│   └── user/UserListDto.java      # createdAt: String (포맷: yyyy-MM-dd)
├── event/
│   ├── UserCreatedEvent.java
│   └── UserAccountEventListener.java  # TODO: 이메일 발송 구현
├── repository/
│   └── UserRepository.java        # search() — LEFT JOIN FETCH
└── service/
    ├── AuthService.java           # login(), changePassword()
    └── EmployeeService.java       # create() → User 자동 생성

product/manager/src/main/resources/
├── application.yml
└── templates/
    ├── pages/auth/
    │   ├── login.html
    │   └── change-password.html
    └── shared/partials/
        └── footer-scripts.html    # axios 전역 설정, 비밀번호 변경 강제 체크
```

---

## 미완료 / 주의 사항

### DB 마이그레이션 (앱 시작 전 필수)
```sql
ALTER TABLE manager.tbl_biz_user
  ADD COLUMN password_change_required boolean NOT NULL DEFAULT false;
```

### TODO
- `UserAccountEventListener.onUserCreated()` — JavaMailSender 또는 외부 메일 서비스 연동
- `UserDetailDto.createdAt` — `LocalDateTime` 타입 그대로, UserListDto처럼 String 포맷 변환 필요
- `UserService.update()` — 관리자가 비밀번호 변경 시 `passwordChangeRequired` 초기화 미처리

---

## 알려진 버그 이력

| 증상 | 원인 | 수정 |
|------|------|------|
| User 목록 미표시 | `list.html` scripts에서 axios 중복 로드 → auth 헤더 초기화 | 중복 `<script>` 제거 |
| 앱 시작 실패 (Jackson 바인딩) | `write-dates-as-timestamps` enum 매핑 실패 (Jackson 3.x) | YAML 속성 제거, DTO 레이어에서 포맷 |
| User 목록 N+1 | `search()` 쿼리 `LEFT JOIN` (FETCH 없음) | `LEFT JOIN FETCH` 로 변경 |