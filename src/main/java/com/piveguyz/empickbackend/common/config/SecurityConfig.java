package com.piveguyz.empickbackend.common.config;

import com.piveguyz.empickbackend.auth.command.jwt.JwtAuthenticationFilter;
import com.piveguyz.empickbackend.security.CustomMemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final CustomMemberDetailsService customMemberDetailsService;

    @Autowired
    public SecurityConfig(CustomMemberDetailsService customMemberDetailsService) {
        this.customMemberDetailsService = customMemberDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .cors(Customizer.withDefaults()) // CORS 적용
                .csrf(csrf -> csrf.disable())    // CSRF 끄기
                .authorizeHttpRequests(auth -> auth
                        // ✅ 인증이 필요 없는 경로 (Swagger, Career 페이지용)
                        .requestMatchers(
                                // Swagger 관련
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",

                                //  Career 페이지 - 채용공고 관련
                                "/api/v1/employment/recruitments",                           // 채용공고 목록
                                "/api/v1/employment/recruitments/**",                       // 채용공고 상세 및 하위 경로
                                "/api/v1/employment/recruitments/requests/**",              // 채용 요청서
                                "/api/v1/employment/recruitments/processes/**",             // 채용 프로세스

                                //  Career 페이지 - 지원자/지원서 관련
                                "/api/v1/employment/applicant/create",                      // 지원자 생성
                                "/api/v1/employment/application",                           // 지원서 생성
                                "/api/v1/employment/application-response",                  // 이력서 응답 저장
                                "/api/v1/employment/applications/items/**",                 // 지원서 항목 조회

                                //  Career 페이지 - 자기소개서 관련
                                "/api/v1/employment/introduce",                             // 자기소개서 생성/조회
                                "/api/v1/employment/introduce/**",                          // 자기소개서 관련 모든 경로
                                "/api/v1/employment/introduce-template/**",                 // 자기소개서 템플릿
                                "/api/v1/employment/introduce-template-item-response",      // 자기소개서 항목별 응답

                                //  Career 페이지 - 마스터 데이터
                                "/api/v1/jobs",                                             // 직무 목록
                                "/api/v1/departments",                                      // 부서 목록

                                //  Career 페이지 - 파일 관련
                                "/api/v1/files/upload",                                     // 파일 업로드 (프로필 이미지)
                                "/api/v1/files/download"                                    // 파일 다운로드
                        ).permitAll()
                        // ✅ 로그인/회원가입 경로는 인증 필요 없음
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // 🔒 그 외 모든 /api/** 경로는 JWT 인증 필터 작동
                        .requestMatchers("/api/**").authenticated()
                        // 🔒 나머지 경로는 기본 인증
                        .anyRequest().authenticated()
                )
                // JWT 인증 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}