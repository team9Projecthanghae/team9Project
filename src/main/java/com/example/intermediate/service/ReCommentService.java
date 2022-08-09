package com.example.intermediate.service;


import com.example.intermediate.controller.request.ReCommentRequestDto;
import com.example.intermediate.controller.response.ReCommentAllResponseDto;
import com.example.intermediate.controller.response.ReCommentResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Like.ReCommentLike;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.ReComment;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.ReCommentRepository;
import com.example.intermediate.repository.like.ReCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReCommentService {

    private final ReCommentRepository recommentRepository;

    private final TokenProvider tokenProvider;

    private final CommentService commentservice;

    private final ReCommentLikeRepository reCommentLikeRepository;

    @Transactional
    public ResponseDto<?> createReComment(ReCommentRequestDto requestDto, HttpServletRequest request) {
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

        Comment comment = commentservice.isPresentComment(requestDto.getCommentId());
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        if(requestDto.getReContent()==null){return ResponseDto.fail("CONTENT_EMPTY", "작성된 글이 없습니다.");
        }

        ReComment reComment = ReComment.builder()
                .member(member)
                .comment(comment)
                .reContent(requestDto.getReContent())
                .build();
        recommentRepository.save(reComment);
        return ResponseDto.success(
                ReCommentResponseDto.builder()
                        .id(reComment.getId())
                        .author(reComment.getMember().getNickname())
                        .reContent(reComment.getReContent())
                        .createdAt(reComment.getCreatedAt())
                        .modifiedAt(reComment.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllReCommentsByPost(Long commentId) {
        Comment comment = commentservice.isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "일치하지 않은 id 입니다.");
        }

        List<ReComment> recommentList = recommentRepository.findAllByComment(comment);
        List<ReCommentResponseDto> recommentResponseDtoList = new ArrayList<>();

        for (ReComment recomment : recommentList) {
            recommentResponseDtoList.add(
                    ReCommentResponseDto.builder()
                            .id(recomment.getId())
                            .author(recomment.getMember().getNickname())
                            .reContent(recomment.getReContent())
                            .createdAt(recomment.getCreatedAt())
                            .modifiedAt(recomment.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(recommentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updateReComment(Long id, ReCommentRequestDto requestDto, HttpServletRequest request) {
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

//        Comment comment = commentService.isPresentComment(requestDto.getCommentId());
//        if (null == comment) {
//            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
//        }

        ReComment recomment = isPresentReComment(id);
        if (null == recomment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 대댓글 id 입니다.");
        }

        if (recomment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }



        recomment.update(requestDto);
        return ResponseDto.success(
                ReCommentResponseDto.builder()
                        .id(recomment.getId())
                        .author(recomment.getMember().getNickname())
                        .reContent(recomment.getReContent())
                        .createdAt(recomment.getCreatedAt())
                        .modifiedAt(recomment.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteReComment(Long id, HttpServletRequest request) {
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

        ReComment recomment = isPresentReComment(id);
        if (null == recomment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 대댓글 id 입니다.");
        }

        if (recomment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "대댓글 작성자만 수정할 수 있습니다.");
        }

        recommentRepository.delete(recomment);
        return ResponseDto.success("success");
    }

    @Transactional(readOnly = true)
    public ReComment isPresentReComment(Long id) {
        Optional<ReComment> optionalReComment = recommentRepository.findById(id);
        return optionalReComment.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    @Transactional(readOnly = true)
    public List<ReCommentAllResponseDto> getAllReCommentsByMember(Member member) {
        List<ReComment> reCommentList = recommentRepository.findAllByMember(member);
        List<ReCommentAllResponseDto> reCommentResponseDtoList = new ArrayList<>();

        for(ReComment reComment : reCommentList) {
            List<ReCommentLike> reCommentLikes = reCommentLikeRepository.findByReComment(reComment);
            int likeCount= reCommentLikes.size();
            reCommentResponseDtoList.add(
                    ReCommentAllResponseDto.builder()
                            .id(reComment.getId())
                            .author(reComment.getMember().getNickname())
                            .reContent(reComment.getReContent())
                            .createdAt(reComment.getCreatedAt())
                            .modifiedAt(reComment.getModifiedAt())
                            .reLikeCount(likeCount)
                            .build()
            );
        }
        return reCommentResponseDtoList;
    }




}
