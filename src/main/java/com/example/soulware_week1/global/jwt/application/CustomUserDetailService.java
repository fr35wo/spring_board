package com.example.soulware_week1.global.jwt.application;

import com.example.soulware_week1.global.jwt.domain.CustomUserDetail;
import com.example.soulware_week1.global.jwt.exception.MemberNotFoundException;
import com.example.soulware_week1.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    //실제 인증 처리 역할
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByUsername(username)
                .map(CustomUserDetail::new)
                .orElseThrow(MemberNotFoundException::new);
    }

//    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
//    private UserDetails createUserDetails(Member member) {
//        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
//                new SimpleGrantedAuthority(member.getRole().name()));
//
//        return User.builder()
//                .username(member.getUsername())
//                .password(passwordEncoder.encode(member.getPassword()))
//                .authorities(authorities)
//                .build();
//    }
}
