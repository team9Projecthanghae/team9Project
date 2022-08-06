package com.example.intermediate.domain;

import com.example.intermediate.controller.request.ReCommentRequestDto;
import com.example.intermediate.domain.Like.ReCommentLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "comment_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    @Column(nullable = false)
    private String reContent;

    @Column
    private int reLikeCount;

    public void update(ReCommentRequestDto reCommentRequestDto ) {
        this.reContent = reCommentRequestDto.getReContent();
    }

   public boolean validateMember(Member member) {
       return !this.member.equals(member);
     }
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reComment", cascade = CascadeType.ALL)
    private List<ReCommentLike> reCommentLikeList = new ArrayList<>();


    public void mappingReCommentLike(ReCommentLike reCommentLike) {
        this.reCommentLikeList.add(reCommentLike);
    }


    public void discountLike(ReCommentLike reCommentLike) {
        this.reCommentLikeList.remove(reCommentLike);

    }
}
