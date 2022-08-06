package com.example.intermediate.service;

import com.example.intermediate.S3.S3Uploader;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.File;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final TokenProvider tokenProvider;

    private final PostService postService;

    private final S3Uploader s3Uploader;

    private final FileRepository  fileRepository;


    public ResponseDto<Object> upload(Long postId, MultipartFile file, HttpServletRequest request) throws IOException {
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

        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        String imageUrl = s3Uploader.uploadFiles(file,"static");

        File newFile = File.builder()
                        .post(post)
                        .url(imageUrl)
                        .build();
        fileRepository.save(newFile);

        return ResponseDto.success(imageUrl);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
