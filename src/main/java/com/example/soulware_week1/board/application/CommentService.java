package com.example.soulware_week1.board.application;


import com.example.soulware_week1.board.api.dto.request.CommentSaveReqDto;
import com.example.soulware_week1.board.api.dto.response.CommentListRspDto;
import com.example.soulware_week1.board.api.dto.response.CommentResDto;
import com.example.soulware_week1.board.domain.Board;
import com.example.soulware_week1.board.domain.Comment;
import com.example.soulware_week1.board.domain.repository.BoardRepository;
import com.example.soulware_week1.board.domain.repository.CommentRepository;
import com.example.soulware_week1.board.exception.BoardNotFoundException;
import com.example.soulware_week1.board.exception.CommentNotFoundException;
import com.example.soulware_week1.board.exception.InvalidPageException;
import com.example.soulware_week1.board.exception.NotCommentOwnerException;
import com.example.soulware_week1.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentResDto insert(Long boardId, CommentSaveReqDto commentSaveReqDto, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        Comment comment = builderComment(commentSaveReqDto, member, board);

        commentRepository.save(comment);

        return CommentResDto.from(comment);
    }

    private Comment builderComment(CommentSaveReqDto commentSaveReqDto, Member member, Board board) {
        return Comment.builder()
                .content(commentSaveReqDto.content())
                .writer(member)
                .board(board)
                .build();
    }

    @Transactional
    public CommentResDto update(Long commentId, CommentSaveReqDto commentSaveReqDto, Member member) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        validateCommentOwner(comment, member);

        comment.updateContent(commentSaveReqDto.content());
        return CommentResDto.from(comment);
    }

    @Transactional
    public void delete(Member member, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        validateCommentOwner(comment, member);

        commentRepository.delete(comment);

    }

    private void validateCommentOwner(Comment comment, Member member) {
        if (!comment.getWriter().getMemberId().equals(member.getMemberId())) {
            throw new NotCommentOwnerException();
        }
    }

    public CommentListRspDto getCommentsByBoard(Long boardId, Pageable pageable, Member member) {
        Page<CommentResDto> commentResDto = commentRepository.findByBoardBoardId(pageable, boardId)
                .map(CommentResDto::from);

        if (commentResDto.getTotalPages() > 0 && pageable.getPageNumber() >= commentResDto.getTotalPages()) {
            throw new InvalidPageException(commentResDto.getTotalPages());
        }

        return CommentListRspDto.of(commentResDto);
    }

}
