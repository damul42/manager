package com.hk.mgmt.service;

import com.hk.mgmt.config.JwtProperties;
import com.hk.mgmt.domain.User;
import com.hk.mgmt.dto.auth.ChangePasswordRequest;
import com.hk.mgmt.dto.auth.LoginRequest;
import com.hk.mgmt.dto.auth.LoginResponse;
import com.hk.mgmt.dto.auth.TokenRefreshRequest;
import com.hk.mgmt.dto.auth.TokenRefreshResponse;
import com.hk.mgmt.repository.UserRepository;
import com.hk.mgmt.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String REFRESH_PREFIX = "refresh:";

    private final AuthenticationManager authenticationManager;
    private final JwtProvider           jwtProvider;
    private final JwtProperties         jwtProperties;
    private final UserRepository        userRepository;
    private final MenuService           menuService;
    private final PasswordEncoder       passwordEncoder;
    private final StringRedisTemplate   redisTemplate;

    // ── 로그인 ────────────────────────────────────────────────────────

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String email = request.email();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        String accessToken  = jwtProvider.createAccessToken(email);
        String refreshToken = jwtProvider.createRefreshToken(email);

        // 리프레시 토큰을 Redis에 저장 (TTL = 7일)
        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + email,
                refreshToken,
                Duration.ofMillis(jwtProperties.refreshTokenExpiration())
        );

        menuService.buildAndCache(user.getId(), email);

        return new LoginResponse(accessToken, refreshToken, user.getId(), user.isPasswordChangeRequired());
    }

    // ── 토큰 갱신 ─────────────────────────────────────────────────────

    public TokenRefreshResponse refresh(TokenRefreshRequest req) {
        String refreshToken = req.refreshToken();

        if (!jwtProvider.isValid(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String email  = jwtProvider.extractUsername(refreshToken);
        String stored = redisTemplate.opsForValue().get(REFRESH_PREFIX + email);

        if (stored == null || !stored.equals(refreshToken)) {
            throw new IllegalArgumentException("만료되었거나 이미 사용된 리프레시 토큰입니다.");
        }

        // 사용자 활성 여부 재확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        if (!user.isEnabled()) {
            redisTemplate.delete(REFRESH_PREFIX + email);
            throw new DisabledException("비활성화된 사용자입니다.");
        }

        // 토큰 로테이션 — 기존 리프레시 토큰 무효화 후 새 토큰 발급
        String newAccessToken  = jwtProvider.createAccessToken(email);
        String newRefreshToken = jwtProvider.createRefreshToken(email);

        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + email,
                newRefreshToken,
                Duration.ofMillis(jwtProperties.refreshTokenExpiration())
        );

        return new TokenRefreshResponse(newAccessToken, newRefreshToken);
    }

    // ── 로그아웃 ──────────────────────────────────────────────────────

    public void logout(String email) {
        menuService.invalidateCache(email);
        redisTemplate.delete(REFRESH_PREFIX + email);
    }

    // ── 비밀번호 변경 ─────────────────────────────────────────────────

    @Transactional
    public void changePassword(String email, ChangePasswordRequest req) {
        if (!req.newPassword().equals(req.confirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        user.changePassword(passwordEncoder.encode(req.newPassword()));
        user.clearPasswordChangeRequired();
    }
}