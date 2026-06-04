package com.hk.mgmt.service;

import com.hk.mgmt.domain.User;
import com.hk.mgmt.dto.auth.ChangePasswordRequest;
import com.hk.mgmt.dto.auth.LoginRequest;
import com.hk.mgmt.dto.auth.LoginResponse;
import com.hk.mgmt.repository.UserRepository;
import com.hk.mgmt.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider           jwtProvider;
    private final UserRepository        userRepository;
    private final MenuService           menuService;
    private final PasswordEncoder       passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String email = request.email();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        menuService.buildAndCache(user.getId(), email);

        return new LoginResponse(
                jwtProvider.createAccessToken(email),
                jwtProvider.createRefreshToken(email),
                user.getId(),
                user.isPasswordChangeRequired()
        );
    }

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