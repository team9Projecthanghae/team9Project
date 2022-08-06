package com.example.intermediate.controller.response;

import com.example.intermediate.domain.Post;
import lombok.*;

import java.util.List;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeReponseDto {
    private List<PostResponseDto> postList;
}
