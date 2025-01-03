package com.example.soulware_week1.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "멤버 id", example = "1")
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    @Schema(description = "이메일", example = "abcd@gmail.com")
    private String username;

    @Column(nullable = false)
    @Schema(description = "비밀번호", example = "1234")
    private String password;

    @Schema(description = "닉네임", example = "asdf")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Schema(description = "권한", example = "ROLE_USER")
    private Role role;

    @Schema(description = "최초 로그인 구분", example = "true, false")
    private boolean firstLogin;

    @Builder
    public Member(Long memberId, String username, String password, String nickname, Role role, Boolean firstLogin) {
        this.memberId = memberId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.firstLogin = firstLogin;
    }

    public void firstLoginUpdate() {
        this.firstLogin = false;
    }
}