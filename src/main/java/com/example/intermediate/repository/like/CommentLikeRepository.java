package com.example.intermediate.repository.like;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Like.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository  extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findById(Long id);
}
