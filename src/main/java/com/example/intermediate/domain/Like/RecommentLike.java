//package com.example.intermediate.domain.Like;
//
//import com.example.intermediate.domain.Member;
//import lombok.*;
//
//import javax.persistence.*;
//
//import static javax.persistence.FetchType.LAZY;
//
//@Entity
//@Getter
//@Builder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class RecommentLike {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "member_id"))
//    private Member member;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "post_id"))
//    private Recomment recomment;
//
//
//    public void mappingUser(Member member) {
//        this.member = member;
//        member.mappingRecommentLike(this);
//    }
//
//    public void mappingRecomment(Recomment recomment) {
//        this.recomment = recomment;
//        comment.mappingRecommentLike(this);
//    }
//}
