package com.example.soulware_week1.board.domain;

import com.example.soulware_week1.board.domain.repository.BoardRepository;
import com.example.soulware_week1.board.factory.BoardFixtureFactory;
import java.util.List;
import java.util.stream.IntStream;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardBulkInsertTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void bulkInsert() {
        EasyRandom easyRandom = BoardFixtureFactory.get();

        int numberOfRecords = 200;
        List<Board> boards = IntStream.range(0, numberOfRecords)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Board.class))
                .toList();

        boardRepository.saveAll(boards);
    }
}
