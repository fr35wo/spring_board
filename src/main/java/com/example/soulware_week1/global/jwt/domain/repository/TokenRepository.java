package com.example.soulware_week1.global.jwt.domain.repository;

import com.example.soulware_week1.global.jwt.domain.Token;
import com.example.soulware_week1.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByMember(Member member);

    Optional<Token> findByRefreshToken(String refreshToken);

    boolean existsByRefreshToken(String refreshToken);

    boolean existsByMember(Member member);
}
