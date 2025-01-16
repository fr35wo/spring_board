package com.example.soulware_week1.global.jwt.application;

import com.example.soulware_week1.global.jwt.JwtTokenProvider;
import com.example.soulware_week1.global.jwt.api.dto.req.RefreshTokenReqDto;
import com.example.soulware_week1.global.jwt.api.dto.req.SignUpReqDto;
import com.example.soulware_week1.global.jwt.api.dto.res.JwtToken;
import com.example.soulware_week1.global.jwt.api.dto.res.MemberTokenResDto;
import com.example.soulware_week1.global.jwt.domain.Token;
import com.example.soulware_week1.global.jwt.domain.repository.TokenRepository;
import com.example.soulware_week1.global.jwt.exception.ExistsUserNameException;
import com.example.soulware_week1.global.jwt.exception.InvalidTokenException;
import com.example.soulware_week1.global.jwt.exception.MemberNotFoundException;
import com.example.soulware_week1.member.domain.Member;
import com.example.soulware_week1.member.domain.Role;
import com.example.soulware_week1.member.domain.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    @Transactional
    public JwtToken signIn(String username, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        Member member = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);

        // 멤버에 대한 기존 토큰 조회
        Optional<Token> existingToken = tokenRepository.findByMember(member);

        if (existingToken.isPresent()) {
            // 기존 토큰이 있으면 업데이트
            Token token = existingToken.get();
            token.updateToken(jwtToken.refreshToken());
            tokenRepository.save(token);
        } else {
            // 기존 토큰이 없으면 새로 생성
            Token tokenEntity = new Token(jwtToken.refreshToken(), member);
            tokenRepository.save(tokenEntity);
        }

        return jwtToken;
    }

    @Transactional(readOnly = false)
    public MemberTokenResDto signUp(SignUpReqDto signUpReqDto) {
        if (memberRepository.existsByUsername(signUpReqDto.username())) {
            throw new ExistsUserNameException();
        }

        String encodedPassword = passwordEncoder.encode(signUpReqDto.password());

        Member member = toEntity(signUpReqDto, encodedPassword);

        return MemberTokenResDto.from(memberRepository.save(member));
    }

    private Member toEntity(SignUpReqDto signUpReqDto, String encodedPassword) {
        return Member.builder()
                .username(signUpReqDto.username())
                .password(encodedPassword)
                .nickname(signUpReqDto.nickname())
                .role(Role.ROLE_USER)
                .build();
    }

    @Transactional
    public JwtToken generateAccessToken(RefreshTokenReqDto refreshTokenReqDto) {
        if (!tokenRepository.existsByRefreshToken(refreshTokenReqDto.refreshToken()) || !jwtTokenProvider.validateToken(
                refreshTokenReqDto.refreshToken())) {
            throw new InvalidTokenException();
        }

        Token token = tokenRepository.findByRefreshToken(refreshTokenReqDto.refreshToken()).orElseThrow();
        Member member = memberRepository.findById(token.getMember().getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        return jwtTokenProvider.generateAccessTokenByRefreshToken(member.getUsername(), token.getRefreshToken());
    }

}
