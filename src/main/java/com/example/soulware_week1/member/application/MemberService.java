package com.example.soulware_week1.member.application;

import com.example.soulware_week1.member.domain.Member;
import com.example.soulware_week1.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public boolean checkAndUpdateFirstLogin(Member member) {
        // 최초 로그인인지 확인
        boolean isFirstLogin = member.isFirstLogin();

        // 최초 로그인일 경우, 상태 업데이트
        if (isFirstLogin) {
            member.firstLoginUpdate(); // 최초 로그인 상태 업데이트
            memberRepository.save(member); // 변경된 상태를 데이터베이스에 저장
        }

        return isFirstLogin;
    }

}
