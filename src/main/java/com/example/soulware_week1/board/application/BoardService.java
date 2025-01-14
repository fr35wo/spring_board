package com.example.soulware_week1.board.application;

import com.example.soulware_week1.board.api.dto.request.BoardSaveReqDto;
import com.example.soulware_week1.board.api.dto.response.BoardListRspDto;
import com.example.soulware_week1.board.api.dto.response.BoardResDto;
import com.example.soulware_week1.board.domain.Board;
import com.example.soulware_week1.board.domain.repository.BoardRepository;
import com.example.soulware_week1.board.exception.BoardNotFoundException;
import com.example.soulware_week1.board.exception.InvalidPageException;
import com.example.soulware_week1.board.exception.NotBoardOwnerException;
import com.example.soulware_week1.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardListRspDto findAll(Pageable pageable) {
        Page<BoardResDto> boardResDto = boardRepository.findAll(pageable)
                .map(BoardResDto::of);

        if (boardResDto.getTotalPages() > 0 && pageable.getPageNumber() >= boardResDto.getTotalPages()) {
            throw new InvalidPageException(boardResDto.getTotalPages());
        }

        return BoardListRspDto.of(boardResDto);
    }

    @Transactional
    public BoardResDto createBoard(BoardSaveReqDto boardSaveReqDto, Member member) {
        Board board = builderBoard(boardSaveReqDto, member);
        Board saveBoard = boardRepository.save(board);
        return BoardResDto.of(saveBoard);
    }

    private Board builderBoard(BoardSaveReqDto boardSaveReqDto, Member member) {
        return Board.builder()
                .title(boardSaveReqDto.title())
                .contents(boardSaveReqDto.contents())
                .member(member)
                .build();
    }

    @Transactional
    public BoardResDto updateBoard(Long boardId, BoardSaveReqDto boardSaveReqDto, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        if (!board.getMember().equals(member)) {
            throw new NotBoardOwnerException();
        }

        board.update(boardSaveReqDto);
        return BoardResDto.of(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        if (!board.getMember().equals(member)) {
            throw new NotBoardOwnerException();
        }

        boardRepository.delete(board);
    }

    public BoardResDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        return BoardResDto.of(board);
    }
}

