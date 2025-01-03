package com.example.soulware_week1.board.domain.repository;

import com.example.soulware_week1.board.domain.Board;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public BoardJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void bulkInsert(List<Board> boards) {
        String sql = "INSERT INTO board (title, contents) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, boards, 1000, (ps, board) -> {
            ps.setString(1, board.getTitle());
            ps.setString(2, board.getContents());
        });
    }
}

