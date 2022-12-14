package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/api/auth/post/{id}/like")
    public ResponseDto<?> pushPostLike(@PathVariable Long id, HttpServletRequest request) {
        return likeService.pushPostLike(id, request);
    }

    @PostMapping("/api/auth/comment/{id}/like")
    public ResponseDto<?> pushCommentLike(@PathVariable Long id, HttpServletRequest request) {
        return likeService.pushCommentLike(id, request);
    }

    @PostMapping("/api/auth/recomments/{id}/like")
    public ResponseDto<?> pushReCommentLike(@PathVariable Long id, HttpServletRequest request) {
        return likeService.pushReCommentLike(id, request);
    }

}


