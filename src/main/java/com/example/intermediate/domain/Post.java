package com.example.intermediate.domain;

import com.example.intermediate.controller.request.PostRequestDto;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.intermediate.domain.Like.PostLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Slf4j
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @OneToMany(fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;


  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = LAZY)
  private Member member;

  public void update(PostRequestDto postRequestDto) {
    this.title = postRequestDto.getTitle();
    this.content = postRequestDto.getContent();
  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }

  @OneToMany(fetch = EAGER, mappedBy = "post", cascade = CascadeType.REMOVE)
  private List<PostLike> postLikeList = new ArrayList<>();


  public void mappingPostLike(PostLike postLike) {
    this.postLikeList.add(postLike);
  }

  public void updateLikeCount() {
    log.info(String.valueOf(this.postLikeList.size()));
  }

  public void discountLike(PostLike postLike) {
    log.info("no");
    this.postLikeList.remove(postLike);
    log.info(String.valueOf(this.postLikeList.size()));

  }
}
