package com.example.soulware_week1.board.domain;

import com.example.soulware_week1.board.api.dto.request.BoardSaveReqDto;
import com.example.soulware_week1.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "BOARD_SEQ_GENERATOR",
        sequenceName = "BOARD_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
    @Column(name = "board_id")
    @Schema(description = "게시판 id", example = "1")
    private Long boardId;

    @Column(nullable = false)
    @Schema(description = "게시글 제목", example = "제목")
    private String title;

    @Column
    @Schema(description = "게시글 내용", example = "내용")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Board(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void update(BoardSaveReqDto boardSaveReqDto) {
        this.title = boardSaveReqDto.title();
        this.contents = boardSaveReqDto.contents();
    }
}
