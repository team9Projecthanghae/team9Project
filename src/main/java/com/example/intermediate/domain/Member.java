package com.example.intermediate.domain;

import com.example.intermediate.domain.Like.CommentLike;
import com.example.intermediate.domain.Like.PostLike;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Member member = (Member) o;
    return id != null && Objects.equals(id, member.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
    return passwordEncoder.matches(password, this.password);
  }
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.REMOVE)
  private List<PostLike> postLikeList = new ArrayList<>();


  public void mappingPostLike(PostLike postLike) {
    this.postLikeList.add(postLike);
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
  private List<CommentLike> commentLikeList = new ArrayList<>();

  public void mappingCommentLike(CommentLike commentLike) {
    this.commentLikeList.add(commentLike);
  }
//
//  @OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.REMOVE)
//  private List<recommentLike> recommentLikeList = new ArrayList<>();
//
//  public void mappingRecommentLike(RecommentLike recommentLike) {
//    this.RecommentLikeList.add(recommentLike);
//  }
}
