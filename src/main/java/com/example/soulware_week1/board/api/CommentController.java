package com.example.soulware_week1.board.api;

import com.example.soulware_week1.board.api.dto.request.CommentSaveReqDto;
import com.example.soulware_week1.board.api.dto.request.PageRequestDto;
import com.example.soulware_week1.board.api.dto.response.CommentListRspDto;
import com.example.soulware_week1.board.api.dto.response.CommentResDto;
import com.example.soulware_week1.board.application.CommentService;
import com.example.soulware_week1.board.domain.Comment;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Tag(name = "comment", description = "Comment Controller")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 등록", description = "댓글 등록 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/{boardId}")
    public ResponseTemplate<CommentResDto> insert(@PathVariable(name = "boardId") Long boardId,
                                                  @Valid @RequestBody CommentSaveReqDto commentSaveReqDto,
                                                  @AuthenticationPrincipal CustomUserDetail member) {

        CommentResDto comments = commentService.insert(boardId, commentSaveReqDto,
                member.getMember());

        return new ResponseTemplate<>(HttpStatus.OK
                , "댓글 작성 완료"
                , comments
        );
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PutMapping("/{commentId}")
    public ResponseTemplate<CommentResDto> update(@PathVariable(name = "commentId") Long commentId,
                                                  @Valid @RequestBody CommentSaveReqDto commentSaveReqDto,
                                                  @AuthenticationPrincipal CustomUserDetail member) {

        CommentResDto commentResDto = commentService.update(commentId, commentSaveReqDto, member.getMember());

        return new ResponseTemplate<>(HttpStatus.OK
                , "댓글 수정 완료"
                , commentResDto);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @DeleteMapping("/{commentId}")
    public ResponseTemplate<Comment> delete(@PathVariable(name = "commentId") Long commentId,
                                            @AuthenticationPrincipal CustomUserDetail member) {
        commentService.delete(member.getMember(), commentId);

        return new ResponseTemplate<>(HttpStatus.OK
                , "댓글 삭제 완료");
    }

    @Operation(summary = "상세 페이지 댓글 조회", description = "상세 페이지의 댓글을 조회 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/{boardId}/comments")
    public ResponseTemplate<CommentListRspDto> getCommentsByBoard(@PathVariable(name = "boardId") Long boardId,
                                                                  @Valid @RequestBody PageRequestDto pageRequestDto,
                                                                  @AuthenticationPrincipal CustomUserDetail member
    ) {
        Pageable pageable = pageRequestDto.toPageable();
        CommentListRspDto commentListRspDto = commentService.getCommentsByBoard(boardId, pageable, member.getMember());

        return new ResponseTemplate<>(HttpStatus.OK, "상세 페이지 댓글 조회 성공", commentListRspDto);
    }

}