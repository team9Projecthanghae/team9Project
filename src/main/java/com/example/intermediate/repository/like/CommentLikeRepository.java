package com.example.intermediate.repository.like;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Like.CommentLike;
import com.example.intermediate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository  extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findById(Long id);
    Optional<CommentLike>findByCommentAndMember(Comment comment, Member member);

    List<CommentLike> findByComment(Comment comment);
}
