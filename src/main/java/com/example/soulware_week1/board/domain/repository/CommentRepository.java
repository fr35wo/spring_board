package com.example.soulware_week1.board.domain.repository;

import com.example.soulware_week1.board.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByBoardBoardId(Pageable pageable, Long boardId);
}
