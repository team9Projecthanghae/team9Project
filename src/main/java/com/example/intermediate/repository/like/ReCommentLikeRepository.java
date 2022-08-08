package com.example.intermediate.repository.like;

import com.example.intermediate.domain.Like.ReCommentLike;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.ReComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReCommentLikeRepository  extends JpaRepository<ReCommentLike, Long> {
    Optional<ReCommentLike> findById(Long id);
    Optional<ReCommentLike> findByReCommentAndMember(ReComment recomment, Member member);
    List<ReCommentLike> findByReComment(ReComment reComment);

    List<ReCommentLike> findReCommentLikesByMember(Member member);
}
