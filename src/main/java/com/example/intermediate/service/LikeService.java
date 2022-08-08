package com.example.intermediate.service;

import com.example.intermediate.controller.response.*;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Like.CommentLike;
import com.example.intermediate.domain.Like.PostLike;
import com.example.intermediate.domain.Like.ReCommentLike;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.domain.ReComment;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.PostRepository;
import com.example.intermediate.repository.ReCommentRepository;
import com.example.intermediate.repository.like.CommentLikeRepository;
import com.example.intermediate.repository.like.PostLikeRepository;
import com.example.intermediate.repository.like.ReCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final ReCommentLikeRepository reCommentLikeRepository;
    private final ReCommentRepository reCommentRepository;

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


    @Transactional
    public ResponseDto<?>pushCommentLike (Long id, HttpServletRequest request) {
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


    public ResponseDto<?>pushReCommentLike (Long id, HttpServletRequest request){
        log.info("1");
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        log.info("2");

        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        ReComment reComment =isPresentReComment(id);
        if(reComment==null){
        log.info("멍청아");}else{
        log.info("마리아");}


        Optional<ReCommentLike> ByReCommentAndMember = reCommentLikeRepository.findByReCommentAndMember(reComment, member);
        ByReCommentAndMember.ifPresentOrElse(
                reCommentLike -> {
                    reCommentLikeRepository.delete(reCommentLike);
                    reComment.discountLike(reCommentLike);
                    log.info("3");
                    },

                    () ->{
                    ReCommentLike reCommentLike = ReCommentLike.builder().build();
                    reCommentLike.mappingReCommentLike(reComment);
                    reCommentLike.mappingMember(member);
                    reCommentLikeRepository.save(reCommentLike);
                        log.info("4");
                    }
                );
        return ResponseDto.success(true);
    }
    @Transactional(readOnly = true)
    public ReComment isPresentReComment(Long id) {
        log.info("뾰잉");
        Optional<ReComment> optionalReComment = reCommentRepository.findById(id);
        return optionalReComment.orElse(null);
    }

    @Transactional
    public List<CommentAllResponseDto> getAllCommentLikesByMember(Member member){
        List<CommentLike> commentLikeList = commentLikeRepository.findCommentLikesByMember(member);
        List<CommentAllResponseDto> commentResponseDtoList = new ArrayList<>();
        for(CommentLike commentLike : commentLikeList) {
            List<CommentLike> commentLikeListByPost = commentLikeRepository.findByComment(commentLike.getComment());
            int likeCount= commentLikeListByPost.size();
            commentResponseDtoList.add(
                    CommentAllResponseDto.builder()
                            .author(commentLike.getComment().getMember().getNickname())
                            .modifiedAt(commentLike.getComment().getModifiedAt())
                            .createdAt(commentLike.getComment().getCreatedAt())
                            .content(commentLike.getComment().getContent())
                            .id(commentLike.getComment().getId())
                            .likeCount(likeCount)
                            .build()
            );
        }
        return commentResponseDtoList;
    }

    @Transactional
    public List<ReCommentAllResponseDto> getAllRecommentLikesByMember(Member member){
        List<ReCommentLike> reCommentLikeList = reCommentLikeRepository.findReCommentLikesByMember(member);
        List<ReCommentAllResponseDto> reCommentAllResponseDtoList = new ArrayList<>();
        for(ReCommentLike reCommentLike : reCommentLikeList) {
            List<ReCommentLike> postLikeListByPost = reCommentLikeRepository.findByReComment(reCommentLike.getReComment());
            int likeCount= postLikeListByPost.size();
            reCommentAllResponseDtoList.add(
                    ReCommentAllResponseDto.builder()
                            .author(reCommentLike.getReComment().getMember().getNickname())
                            .modifiedAt(reCommentLike.getReComment().getModifiedAt())
                            .createdAt(reCommentLike.getReComment().getCreatedAt())
                            .reContent(reCommentLike.getReComment().getReContent())
                            .id(reCommentLike.getReComment().getId())
                            .reLikeCount(likeCount)
                            .build()
            );
        }
        return reCommentAllResponseDtoList;
    }
}
