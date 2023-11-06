package com.diareat.diareat.config;

import com.diareat.diareat.auth.component.JwtAuthFilter;
import com.diareat.diareat.auth.component.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] AUTH_LIST = { // swagger 관련 URl
            "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**", "/actuator/health"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_LIST).permitAll() // swagger 관련 URL은 인증 없이 접근 가능 (테스트용)
                .antMatchers("/api/auth/**").permitAll() // 회원가입/로그인 관련 URL은 인증 없이 접근 가능
                .anyRequest().authenticated() // 나머지 모든 URL은 Jwt 인증 필요
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
