package com.example.intermediate.domain.Like;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import lombok.*;

import javax.persistence.*;

import java.util.Optional;

import static javax.persistence.FetchType.LAZY;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_CommentLike_Member"))
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "FK_CommentLike_Member"))
    private Comment comment;

    public static boolean isVotedComment(Optional<CommentLike> optionalCommentLike) {
        return optionalCommentLike.isPresent();
    }
//
//    public void mappingMember(Member member) {
//        this.member = member;
//        member.mappingCommentLike(this);
//    }
//
//    public void mappingComment(Comment comment) {
//        this.comment = comment;
//        comment.mappingCommentLike(this);
//    }

}
