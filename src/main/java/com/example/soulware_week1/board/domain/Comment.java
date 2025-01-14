package com.example.soulware_week1.board.domain;

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
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert//Hibernate의 어노테이션으로, insert SQL을 실행할 때 null인 필드를 제외합니다. 이를 통해 SQL을 최적화할 수 있습니다.
@SequenceGenerator(
        name = "COMMENT_SEQ_GENERATOR",
        sequenceName = "COMMENT_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    @Column(name = "comment_id")
    @Schema(description = "댓글 id", example = "1")
    private Long commentId;

    @Schema(description = "댓글 내용", example = "댓글입니다")
    @Column(nullable = false, length = 255)
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(String content, Member writer, Board board) {
        this.content = content;
        this.writer = writer;
        this.board = board;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}

