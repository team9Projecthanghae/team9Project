package com.example.intermediate.service;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Like.PostLike;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.PostRepository;
import com.example.intermediate.repository.like.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;
    private final PostLikeRepository postLikeRepository;


    public ResponseDto<?> pushpostlike(Long postId, HttpServletRequest request) {

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

        if (null == post) {
            return ResponseDto.fail("UNAUTHORIZED_CONTACT",
                    "본인이 작성한 글이 아닙니다.");
        }
        if (!Objects.equals(member.getId(), post.getMember().getId())) {
            return ResponseDto.fail("INVALID_INFORMATION",
                    "다시 로그인 해주십시오.");
        }
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
                    post.updateLikeCount();

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


}
