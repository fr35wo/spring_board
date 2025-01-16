package com.example.soulware_week1.board.application;

import com.example.soulware_week1.board.domain.Board;
import com.example.soulware_week1.board.domain.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransactionalBoardService {
    private final BoardRepository boardRepository;

    public TransactionalBoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }
}
