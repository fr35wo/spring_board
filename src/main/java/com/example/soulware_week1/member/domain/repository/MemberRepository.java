package com.example.soulware_week1.member.domain.repository;

import com.example.soulware_week1.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT MAX(m.memberId) FROM Member m")
    Optional<Long> findMaxId();
}
