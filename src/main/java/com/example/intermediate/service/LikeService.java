package com.example.intermediate.service;

import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.PostLikeReponseDto;
import com.example.intermediate.controller.response.PostResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Like.CommentLike;
import com.example.intermediate.domain.Like.PostLike;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.PostRepository;
import com.example.intermediate.repository.like.CommentLikeRepository;
import com.example.intermediate.repository.like.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;
    private final PostLikeRepository postLikeRepository;
private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

//    private final RecommentRepository recommentRepository;

    private final PostService postService;


    @Transactional
    public ResponseDto<?> pushPostLike(Long postId, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);

        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(postId);

        Optional<PostLike> ByPostAndMember = postLikeRepository.findByPostAndMember(post, member);

        ByPostAndMember.ifPresentOrElse(
                // 좋아요 있을경우 삭제
                postLike -> {
                    postLikeRepository.delete(postLike);
                    post.discountLike(postLike);


                },
                // 좋아요가 없을 경우 좋아요 추가
                () -> {
                    PostLike postLike = PostLike.builder().build();

                    postLike.mappingPost(post);
                    postLike.mappingMember(member);

                    postLikeRepository.save(postLike);
                });
        return ResponseDto.success(true);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional
    public ResponseDto<?>pushCommentLike (Long id, HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
    public List<PostResponseDto> getAllPostLikesByMember(Member member){
       List<PostLike> postLikeList = postLikeRepository.findPostLikesByMember(member);
       List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(PostLike postLike : postLikeList) {
            String url = postService.getImageUrlByPost(postLike.getPost());
            List<PostLike> postLikeListByPost = postLikeRepository.findAllByPost(postLike.getPost());
            int likeCount= postLikeListByPost.size();
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .title(postLike.getPost().getTitle())
                            .author(postLike.getPost().getMember().getNickname())
                            .modifiedAt(postLike.getPost().getModifiedAt())
                            .createdAt(postLike.getPost().getCreatedAt())
                            .content(postLike.getPost().getContent())
                            .id(postLike.getPost().getId())
                            .imageUrl(url)
                            .likeCount(likeCount)
                            .build()
            );
        }
        return postResponseDtoList;
    }


        Member member = validateMember(request);

        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment =isPresentComment(id);

        Optional<CommentLike> ByCommentAndMember = commentLikeRepository.findByCommentAndMember(comment, member);
        ByCommentAndMember.ifPresentOrElse(
                commentLike -> {
                    commentLikeRepository.delete(commentLike);
                    comment.discountLike(commentLike);
                },
                () ->{
                    CommentLike commentLike = CommentLike.builder().build();
                    commentLike.mappingComment(comment);
                    commentLike.mappingMember(member);

                    commentLikeRepository.save(commentLike);
                }
        );
        return ResponseDto.success(true);
    }
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

//    public ResponseDto<?>pushRecommentLike (Long id, HttpServletRequest request){
//        if (null == request.getHeader("Refresh-Token")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        Member member = validateMember(request);
//
//        if (null == member) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//        }
//
//        Recomment recomment =isPresentRecomment(id);
//
//        Optional<RecommentLike> ByRecommentAndMember = recommentRepository.findByRecommentAndMember(recomment, member);
//        ByRecommentAndMember.ifPresentOrElse(
//                recommentLike -> {
//                    recommentLikeRepository.delete(recommentLike);
//                    recomment.discountLike(recommentLike);
//                    },
//                    () ->{
//                    RecommentLike recommentLike = RecommentLike.builder().build();
//                    recommentLike.mappingRecomment(recomment);
//                    recommentLike.mappingMember(member);
//
//                    recommentLikeRepository.save(recommentLike);
//                    }
//                );
//        return ResponseDto.success(true);
//    }
//    @Transactional(readOnly = true)
//    public Recomment isPresentRecomment(Long id) {
//        Optional<Recomment> optionalRecomment = recommentRepository.findById(id);
//        return optionalRecomment.orElse(null);
//    }
}
