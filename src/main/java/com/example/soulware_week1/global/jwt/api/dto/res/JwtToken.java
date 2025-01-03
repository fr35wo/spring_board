package com.example.soulware_week1.global.jwt.api.dto.res;

import lombok.Builder;

//클라이언트에게 토큰을 보내기 위한 DTO
@Builder
public record JwtToken(
        String accessToken,
        String refreshToken,
        String userNickname
) {

}