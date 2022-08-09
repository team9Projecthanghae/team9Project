package com.example.intermediate.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageReponseDto {
    private List<CommentResponseDto> commentResponseDtoList;
    private List<PostResponseDto> postResponseDtoList;
    private List<PostResponseDto> postLikeResponseDtoList;
    private List<ReCommentAllResponseDto> reCommentAllResponseDtoList;
    private List<CommentAllResponseDto> commentLikeAllResponseDtoList;
    private List<ReCommentAllResponseDto> reCommentLikeAllResponseDtoList;

}
