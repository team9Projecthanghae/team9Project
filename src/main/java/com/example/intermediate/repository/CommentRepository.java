package com.example.intermediate.repository;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByPost(Post post);
  List<Comment> findAllByMember(Member member);
}
