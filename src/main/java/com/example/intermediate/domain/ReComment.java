package com.example.intermediate.domain;

import com.example.intermediate.controller.request.ReCommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @JoinColumn(name = "comment_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post comment;

    @JoinColumn(name = "comment_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post content;

//    @Column(nullable = false)
//    private Long LikeCounter;

    @Column(nullable = false)
    private String recomment;


    public void update(ReCommentRequestDto recommentRequestDto) {
        this.recomment = recommentRequestDto.getrecomment();
    }

   public boolean validateMember(Member member) {
       return !this.member.equals(member);
     }
}
