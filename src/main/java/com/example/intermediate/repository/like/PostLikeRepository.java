package com.example.intermediate.repository.like;

import com.example.intermediate.domain.Like.PostLike;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository  extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findById(Long id);
    Optional<PostLike> findByPostAndMember(Post post, Member member);
    List<PostLike> findAllByPost(Post post);

    List<PostLike> findPostLikesByMember (Member member);
}
