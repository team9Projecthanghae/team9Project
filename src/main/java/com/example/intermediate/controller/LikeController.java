package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.UserDetailsImpl;
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

    @PostMapping("/api/post/{id}/like")
    public ResponseDto<?> pushpostlike(@PathVariable Long id, HttpServletRequest request) {
        likeService.pushpostlike(id, request);
        return postService.getPost(id);
    }

//    @PostMapping("/api/comment/{id}/like")
//    public ResponseDto<?> pushcommentlike(@PathVariable Long id, UserDetailsImpl userDetails,  HttpServletRequest request) {
//        String nickname =userDetails.getMember().getNickname();
//        likeService.pushcommentlike(id, nickname, request);
//        return commentService.getComment(id);
//    }

//    @PostMapping("/api/recomment/{id}/like")
//    public ResponseDto<?> pushrecommentlike(@PathVariable Long id, UserDetailsImpl userDetails) {
//        return recommentService.getrecomment(id);
//    }

}


