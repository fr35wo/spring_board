package com.example.soulware_week1.member.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    @Schema(description = "멤버 id", example = "1")
    @Column(name = "member_id")
    private Long memberId;

    @NotBlank(message = "아이디는 비워둘 수 없습니다.")
    @Column(nullable = false, unique = true)
    @Schema(description = "이메일", example = "abcd@gmail.com")
    private String username;

    @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
    @Column(nullable = false)
    @Schema(description = "비밀번호", example = "1234")
    private String password;

    @Schema(description = "닉네임", example = "asdf")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Schema(description = "권한", example = "ROLE_USER")
    private Role role;

    @Builder
    public Member(Long memberId, String username, String password, String nickname, Role role) {
        this.memberId = memberId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId) && Objects.equals(username, member.username)
                && Objects.equals(password, member.password) && Objects.equals(nickname,
                member.nickname) && role == member.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, username, password, nickname, role);
    }
}