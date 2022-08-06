//package com.example.intermediate.repository.like;
//
//import com.example.intermediate.domain.Comment;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface RecommentLikeRepository  extends JpaRepository<Recomment, Long> {
//    List<RecommentLike> findById(Long id);
//    Optional<RecommentLike> findByRecommentAndMember(Recomment recomment, Member member);
//}
