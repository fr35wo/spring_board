package com.example.soulware_week1.global.jwt.api;


import com.example.soulware_week1.global.jwt.api.dto.req.RefreshTokenReqDto;
import com.example.soulware_week1.global.jwt.api.dto.req.SignInReqDto;
import com.example.soulware_week1.global.jwt.api.dto.req.SignUpReqDto;
import com.example.soulware_week1.global.jwt.api.dto.res.JwtToken;
import com.example.soulware_week1.global.jwt.api.dto.res.MemberTokenResDto;
import com.example.soulware_week1.global.jwt.application.TokenService;
import com.example.soulware_week1.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth Controller")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Operation(summary = "자체로그인 후 토큰 발급", description = "액세스, 리프레쉬 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    })
    @PostMapping("/sign-in")
    public RspTemplate<JwtToken> signIn(@RequestBody @Valid SignInReqDto signInReqDto) {
        String username = signInReqDto.username();
        String password = signInReqDto.password();
        JwtToken jwtToken = tokenService.signIn(username, password);

        return new RspTemplate<>(HttpStatus.OK, "로그인 성공", jwtToken);
    }

    @Operation(summary = "회원가입", description = "회원가입 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    @PostMapping("/sign-up")
    public RspTemplate<MemberTokenResDto> signUp(@RequestBody SignUpReqDto signUpReqDto) {
        MemberTokenResDto memberTokenResDto = tokenService.signUp(signUpReqDto);
        return new RspTemplate<>(HttpStatus.OK, "회원가입 성공", memberTokenResDto);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "리프레쉬 토큰으로 액세스 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    })
    @PostMapping("/access")
    public RspTemplate<JwtToken> generateAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        JwtToken getToken = tokenService.generateAccessToken(refreshTokenReqDto);

        return new RspTemplate<>(HttpStatus.OK, "액세스 토큰 발급", getToken);
    }

}
