package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.CommentService;
import com.example.intermediate.service.LikeService;
import com.example.intermediate.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;
    private final PostService postService;
    private final CommentService commentService;
//    private  final RecommentService recommentService;

    @PostMapping("/api/auth/post/{id}/like")
    public ResponseDto<?> pushPostLike(@PathVariable Long id, HttpServletRequest request) {
        return likeService.pushPostLike(id, request);
    }

    @PostMapping("/api/auth/comment/{id}/like")
    public ResponseDto<?> pushCommentLike(@PathVariable Long id, HttpServletRequest request) {
        return likeService.pushCommentLike(id, request);
    }

//    @PostMapping("/api/recomment/{id}/like")
//    public ResponseDto<?> pushrecommentlike(@PathVariable Long id, HttpServletRequest request) {
//        return likeService.pushCRecommentLike(id, request);
//    }

}


