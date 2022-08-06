package com.example.intermediate.domain;

import com.example.intermediate.controller.request.PostRequestDto;
import com.example.intermediate.domain.Like.PostLike;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

  @OneToMany(fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;


  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = LAZY)
  private Member member;

  @OneToOne(mappedBy = "post", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private File file;

  public void update(PostRequestDto postRequestDto) {
    this.title = postRequestDto.getTitle();
    this.content = postRequestDto.getContent();
  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }

  @OneToMany(fetch = LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
  private List<PostLike> postLikeList = new ArrayList<>();


  public void mappingPostLike(PostLike postLike) {
    this.postLikeList.add(postLike);
  }


  public void discountLike(PostLike postLike) {
    this.postLikeList.remove(postLike);
  }
}
