package com.example.intermediate.service;


import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.ReCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReCommentService {
    private final ReCommentRepository recommentRepository;

    private final TokenProvider tokenProvider;

    private final PostService postService;



}
