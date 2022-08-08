package com.example.intermediate.domain.Like;

import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.ReComment;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "re_comment_id")
    private ReComment reComment;


    public void mappingMember(Member member) {
        this.member = member;
        member.mappingReCommentLike(this);
    }

    public void mappingReCommentLike(ReComment reComment) {
        this.reComment = reComment;
        reComment.mappingReCommentLike(this);
    }
}
