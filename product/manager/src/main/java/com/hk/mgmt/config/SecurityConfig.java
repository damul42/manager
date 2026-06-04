package com.hk.mgmt.config;

import com.hk.mgmt.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**", "/js/**", "/images/**",
                                "/plugins/**", "/data/**", "/favicon.ico"
                        ).permitAll()
                        .requestMatchers(
                                "/api/auth/**", "/login",
                                "/auth/**", "/auth-card/**", "/auth-split/**",
                                "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            String accept = request.getHeader(HttpHeaders.ACCEPT);
                            boolean isApiRequest = request.getRequestURI().startsWith("/api/");
                            boolean isJsonRequest = accept != null
                                    && accept.contains(MediaType.APPLICATION_JSON_VALUE);

                            if (isApiRequest || isJsonRequest) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType(
                                        MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
                                response.getWriter().write("{\"message\":\"인증이 필요합니다.\"}");
                            } else {
                                String returnUrl = request.getRequestURI();
                                String query = request.getQueryString();
                                if (query != null) returnUrl += "?" + query;
                                response.sendRedirect("/login?returnUrl="
                                        + URLEncoder.encode(returnUrl, StandardCharsets.UTF_8));
                            }
                        })
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * JwtAuthenticationFilter가 @Component이므로 서블릿 필터로 자동 등록된다.
     * Security 체인에서만 동작하도록 자동 등록을 비활성화한다.
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setEnabled(false);
        return reg;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}