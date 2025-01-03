package com.example.soulware_week1.board.application;

import com.example.soulware_week1.board.api.dto.request.BoardSaveReqDto;
import com.example.soulware_week1.board.api.dto.response.BoardDto;
import com.example.soulware_week1.board.api.dto.response.BoardListRspDto;
import com.example.soulware_week1.board.api.dto.response.BoardResDto;
import com.example.soulware_week1.board.domain.Board;
import com.example.soulware_week1.board.domain.repository.BoardRepository;
import com.example.soulware_week1.board.exception.BoardNotFoundException;
import com.example.soulware_week1.board.exception.InvalidPageException;
import com.example.soulware_week1.board.exception.NotBoardOwnerException;
import com.example.soulware_week1.global.jwt.domain.CustomUserDetail;
import com.example.soulware_week1.global.jwt.exception.MemberNotFoundException;
import com.example.soulware_week1.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardListRspDto findAll(Pageable pageable, CustomUserDetail member) {
        if (member == null) {
            throw new MemberNotFoundException();
        }

        Page<BoardDto> boardDtos = boardRepository.findAll(pageable)
                .map(BoardDto::fromEntity);

        if (boardDtos.getTotalPages() > 0 && pageable.getPageNumber() >= boardDtos.getTotalPages()) {
            throw new InvalidPageException(boardDtos.getTotalPages());
        }

        return BoardListRspDto.of(boardDtos);
    }

    public Pageable createPageable(int page, int size) {
        if (page < 1 || size < 1) {
            throw new InvalidPageException(page, size);
        }
        return PageRequest.of(page - 1, size, Sort.by("boardId").descending());
    }

    @Transactional
    public BoardResDto createBoard(BoardSaveReqDto boardSaveReqDto, Member member) {
        if (member == null) {
            throw new MemberNotFoundException();
        }

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
        if (member == null) {
            throw new MemberNotFoundException();
        }

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
        if (member == null) {
            throw new MemberNotFoundException();
        }

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

