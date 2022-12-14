package com.example.intermediate.domain;

import com.example.intermediate.controller.request.PostRequestDto;
import com.example.intermediate.domain.Like.PostLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

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

  @Column(nullable = false)
  private String imageUrl;


  @OneToMany(fetch = LAZY,mappedBy = "post",cascade = CascadeType.ALL, orphanRemoval = true)
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


  @OneToMany(fetch = LAZY, mappedBy = "post", cascade = CascadeType.ALL)
  private List<PostLike> postLikeList = new ArrayList<>();


  public void discountLike(PostLike postLike) {
    this.postLikeList.remove(postLike);
  }
}
