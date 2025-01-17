package com.example.soulware_week1.global.jwt.domain;

import com.example.soulware_week1.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@SequenceGenerator(
        name = "TOKEN_SEQ_GENERATOR",
        sequenceName = "TOKEN_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOKEN_SEQ_GENERATOR")
    private Long tokenId;

    @Column(nullable = false)
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Builder
    public Token(String refreshToken, Member member) {
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public void updateToken(String refreshToken) {
        if (!this.refreshToken.equals(refreshToken)) {
            this.refreshToken = refreshToken;
        }
    }

}
