package com.example.soulware_week1.global.jwt.api.dto.req;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public record SignUpReqDto(
        @NotBlank(message = "아이디는 비워둘 수 없습니다.")
        String username,

        @NotBlank(message = "닉네임은 비워둘 수 없습니다.")
        String nickname,

        @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
        String password,

        @NotBlank(message = "비밀번호 확인은 비워둘 수 없습니다.")
        String passwordCheck
) {
    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatched() {
        return password != null && password.equals(passwordCheck);
    }
}

