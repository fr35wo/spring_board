package com.example.soulware_week1.global.dummy;

import com.example.soulware_week1.board.domain.Board;
import com.example.soulware_week1.board.domain.Comment;
import com.example.soulware_week1.global.jwt.JwtTokenProvider;
import com.example.soulware_week1.global.jwt.domain.Token;
import com.example.soulware_week1.member.domain.Member;
import com.example.soulware_week1.member.domain.Role;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DummyDataService {

    private final EasyRandom easyRandom;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final @Qualifier("masterDataSource") HikariDataSource hikariDataSource;

    @Autowired
    public DummyDataService(JwtTokenProvider jwtTokenProvider,
                            PasswordEncoder passwordEncoder,
                            HikariDataSource hikariDataSource,
                            EntityManager entityManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.hikariDataSource = hikariDataSource;
        this.entityManager = entityManager;

        this.easyRandom = new EasyRandom(new EasyRandomParameters()
                .stringLengthRange(5, 15)
                .randomize(Long.class, () -> null)
                .excludeField(field -> field.getName().equals("member"))
                .excludeField(field -> field.getName().equals("board"))
                .excludeField(field -> field.getName().equals("writer")));
    }

    @Async("asyncExecutor")
    @Transactional
    public void insertTestDataAsync() {
        String threadName = Thread.currentThread().getName();
        log.info("Thread Name: {}", threadName);
        log.info("HikariCP - Active Connections: {}", hikariDataSource.getHikariPoolMXBean().getActiveConnections());
        log.info("HikariCP - Idle Connections: {}", hikariDataSource.getHikariPoolMXBean().getIdleConnections());
        log.info("HikariCP - Total Connections: {}", hikariDataSource.getHikariPoolMXBean().getTotalConnections());

        Member member = Member.builder()
                .username(easyRandom.nextObject(String.class))
                .password(passwordEncoder.encode(easyRandom.nextObject(String.class)))
                .nickname(easyRandom.nextObject(String.class))
                .role(Role.ROLE_USER)
                .build();
        entityManager.persist(member); // persist로 1차 캐시에 저장

        String refreshToken = jwtTokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(
                        member.getUsername(), null, List.of(new SimpleGrantedAuthority(Role.ROLE_USER.toString()))
                )
        ).refreshToken();

        Token token = Token.builder()
                .refreshToken(refreshToken)
                .member(member)
                .build();
        entityManager.persist(token);

        for (int j = 0; j < 50; j++) {
            Board board = Board.builder()
                    .title(easyRandom.nextObject(String.class))
                    .contents(easyRandom.nextObject(String.class))
                    .member(member)
                    .build();
            entityManager.persist(board);

            for (int k = 0; k < 50; k++) {
                Comment comment = Comment.builder()
                        .content(easyRandom.nextObject(String.class))
                        .writer(member)
                        .board(board)
                        .build();
                entityManager.persist(comment);

                if ((k + 1) % 50 == 0) { // 배치 크기마다 flush 및 clear 호출
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}
