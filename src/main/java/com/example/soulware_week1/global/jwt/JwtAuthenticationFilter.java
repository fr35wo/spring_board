package com.example.soulware_week1.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

//클라이언트 요청 시 JWT 인증을 하기 위해 설치하는 커스텀 필터로, UsernamePasswordAuthenticationFilter 이전에 실행 할 것이다.
//클라이언트로부터 들어오는 요청에서 JWT 토큰을 처리하고,
// 유효한 토큰인 경우 해당 토큰의 인증 정보(Authentication)를 SecurityContext에 저장하여 인증된 요청을 처리할 수 있도록 한다.
//JWT를 통해 username + password 인증을 수행
//SecurityContextPersistenceFilter는 상태 저장 세션 관리에서 사용됩니다.
//SessionCreationPolicy.STATELESS 설정에서는 SecurityContextPersistenceFilter가 포함되지 않으며, SecurityContextHolder에 인증 정보를 저장하는 책임은 개발자가 직접 관리해야 합니다.
//**JwtAuthenticationFilter**에서 인증 정보를 수동으로 저장하고, 요청 종료 시 초기화합니다.

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 2. validateToken으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 수동으로 저장
            //현재 Authentication 객체는 인증 전 토큰 객체
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        // 1. Request Header에서 JWT 토큰 추출
//        String token = resolveToken((HttpServletRequest) request);
//
//        // 2. validateToken으로 토큰 유효성 검사
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        chain.doFilter(request, response);
//    }

}
