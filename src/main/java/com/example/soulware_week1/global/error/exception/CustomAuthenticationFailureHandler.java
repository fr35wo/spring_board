package com.example.soulware_week1.global.error.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationFailureHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> data = new HashMap<>();
        data.put("status", HttpServletResponse.SC_UNAUTHORIZED);

        // 인증 실패 유형에 따른 메시지 구분
        if (authException instanceof BadCredentialsException) {
            data.put("message", "아이디나 비밀번호가 올바르지 않습니다.");
        } else if (authException instanceof CredentialsExpiredException) {
            data.put("message", "인증 정보가 만료되었습니다.");
        } else {
            data.put("message", "인증에 실패하였습니다."); // 기본 메시지
        }

        OutputStream out = response.getOutputStream();
        objectMapper.writeValue(out, data);
        out.flush();
    }
}
