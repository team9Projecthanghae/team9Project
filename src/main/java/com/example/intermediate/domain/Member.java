package com.example.intermediate.domain;

import com.example.intermediate.domain.Like.CommentLike;
import com.example.intermediate.domain.Like.PostLike;
import com.example.intermediate.domain.Like.ReCommentLike;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
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

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<PostLike> postLikeList = new HashSet<>();

    @Transactional
    public void mappingPostLike(PostLike postLike) {
        this.postLikeList.add(postLike);
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<CommentLike> commentLikeList = new HashSet<>();

    @Transactional
    public void mappingCommentLike(CommentLike commentLike) {
        this.commentLikeList.add(commentLike);
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.REMOVE)
    private Set<ReCommentLike> reCommentLikeList = new HashSet<>();

    public void mappingReCommentLike(ReCommentLike reCommentLike) {
        this.reCommentLikeList.add(reCommentLike);
    }
}
