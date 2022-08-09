package com.example.intermediate.domain.Like;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Member;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comments_id")
    private Comment comment;

    @Transactional
    public void mappingMember(Member member) {
        this.member = member;
        member.mappingCommentLike(this);
    }

    @Transactional
    public void mappingComment(Comment comment) {
        this.comment = comment;
        comment.mappingCommentLike(this);
    }

}
