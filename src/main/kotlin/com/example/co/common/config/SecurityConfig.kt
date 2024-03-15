package com.example.co.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        return http
            // CSRF 보안 설정 (클라이언트에서 csrf 토큰을 전송하지 않아도 됨)
            .csrf { it.disable() }
            .headers {
                it.frameOptions { frameOptionsConfig ->
                    // /h2-console 화면 깨짐 현상 방지
                    // /h2-console 페이지를 iframe으로 렌더링할 때 출처가 같은 경우에만 허용
                    frameOptionsConfig.sameOrigin()
                }
            }
            // HTTP 요청에 대한 인가 규칙 설정
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/assets/**",
                    "/h2-console/**",
                    "/accounts/**",
                    "/demo/**",
                    "/test/**",
                    "/error"
                ).permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/user")
                    .permitAll()
                    .requestMatchers("/api/user**")
                    .hasRole("ADMIN")
                    .anyRequest().fullyAuthenticated() // 나머지 요청은 인증된 사용자에게만 허용
            }
            .formLogin {
                it.loginPage("/login.html") // 로그인 페이지 URL 지정
                    .permitAll() // 로그인 페이지는 모든 사용자에게 허용
            }
            .logout {
                it.logoutUrl("/logout.html") // 로그아웃 URL 지정
                    .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 페이지 지정
                    .invalidateHttpSession(true) // HTTP 세션 무효화 여부
                    .deleteCookies("JSESSIONID") // 삭제할 쿠키 지정
            }
            .sessionManagement {
                // always 세션 생성, never 세션 생성 안함, stateless 세션 생성 안함
                // JWT 토큰 기반에서는 세션 생성 안함
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .build()
    }
}