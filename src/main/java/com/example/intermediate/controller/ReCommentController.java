package com.example.intermediate.controller;

import com.example.intermediate.controller.request.ReCommentRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.ReCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController

public class ReCommentController {

    private final ReCommentService recommentService;

    @PostMapping("/api/auth/recomments")
    public ResponseDto<?> createReComment(@RequestBody ReCommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return recommentService.createReComment(requestDto, request);
    }


    @GetMapping("/api/recomments/{id}")
    public ResponseDto<?> getAllReComments(@PathVariable Long id) {
        return recommentService.getAllReCommentsByPost(id);
    }


    @PutMapping("/api/auth/recomments/{id}")
    public ResponseDto<?> updateReComment(@PathVariable Long id, @RequestBody ReCommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return recommentService.updateReComment(id, requestDto, request);
    }


    @DeleteMapping("/api/auth/recomments/{id}")
    public ResponseDto<?> deleteReComment(@PathVariable Long id, HttpServletRequest request) {
        return recommentService.deleteReComment(id, request);
    }
}

