package com.example.soulware_week1.board.api;

import com.example.soulware_week1.board.api.dto.request.BoardSaveReqDto;
import com.example.soulware_week1.board.api.dto.request.PageRequestDto;
import com.example.soulware_week1.board.api.dto.response.BoardListRspDto;
import com.example.soulware_week1.board.api.dto.response.BoardResDto;
import com.example.soulware_week1.board.application.BoardService;
import com.example.soulware_week1.global.jwt.domain.CustomUserDetail;
import com.example.soulware_week1.global.template.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
@Tag(name = "board", description = "Board Controller")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "게시물 조회", description = "전체 게시물 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BoardListRspDto.class))),
    })
    @PostMapping("/list")
    public ResponseTemplate<BoardListRspDto> handleGetAllBoard(
            @Valid @RequestBody PageRequestDto pageRequestDto,
            @AuthenticationPrincipal CustomUserDetail member
    ) {
        Pageable pageable = pageRequestDto.toPageable();

        BoardListRspDto boardListRspDto = boardService.findAll(pageable);

        return new ResponseTemplate<>(HttpStatus.OK
                , pageRequestDto.page() + "번 페이지 조회 완료"
                , boardListRspDto
        );
    }

    @Operation(summary = "게시물 등록", description = "게시물 등록합니다.")
    @PostMapping
    public ResponseTemplate<BoardResDto> createBoard(
            @Valid @RequestBody BoardSaveReqDto boardRequestDto,
            @AuthenticationPrincipal CustomUserDetail member
    ) {
        BoardResDto boardResDto = boardService.createBoard(boardRequestDto, member.getMember());
        return new ResponseTemplate<>(HttpStatus.OK
                , boardResDto.boardId() + "번 게시판 등록 완료"
                , boardResDto
        );
    }

    @Operation(summary = "게시물 수정", description = "게시물 수정합니다.")
    @PutMapping
    public ResponseTemplate<BoardResDto> updateBoard(
            @RequestParam("boardId") Long boardId,
            @Valid @RequestBody BoardSaveReqDto boardRequestDto,
            @AuthenticationPrincipal CustomUserDetail member
    ) {
        BoardResDto boardResDto = boardService.updateBoard(boardId, boardRequestDto, member.getMember());
        return new ResponseTemplate<>(HttpStatus.OK
                , boardId + "번 게시물 수정 완료"
                , boardResDto);
    }

    @Operation(summary = "게시물 삭제", description = "게시물 삭제합니다.")
    @DeleteMapping
    public ResponseTemplate<Void> deleteBoard(
            @RequestParam("boardId") Long boardId,
            @AuthenticationPrincipal CustomUserDetail member
    ) {
        boardService.deleteBoard(boardId, member.getMember());
        return new ResponseTemplate<>(HttpStatus.OK, boardId + "번 게시물 삭제 완료");
    }

    @Operation(summary = "상세 페이지", description = "상세 페이지로 넘겨줍니다.")
    @GetMapping
    public ResponseTemplate<BoardResDto> getBoard(
            @RequestParam("boardId") Long boardId,
            @AuthenticationPrincipal CustomUserDetail member
    ) {
        BoardResDto boardResDto = boardService.getBoard(boardId);
        return new ResponseTemplate<>(HttpStatus.OK
                , boardId + "상세뷰 확인 완료"
                , boardResDto
        );
    }
}

