package com.example.soulware_week1.board.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BoardSequenceTest {

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    @Commit
    public void testSequenceStrategy() {

        Board board = Board.builder()
                .title("제목")
                .contents("내용")
                .build();

        em.persist(board);
        System.out.println("=== After persist() ===");
        System.out.println("Generated ID: " + board.getBoardId());

        System.out.println("=== Before commit ===");
    }
}

