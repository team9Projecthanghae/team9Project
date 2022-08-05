package com.example.intermediate.controller;

import com.example.intermediate.S3.S3Uploader;
import com.example.intermediate.controller.request.LoginRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @RequestMapping(value = "/api/auth/image", method = RequestMethod.POST)
    public ResponseDto<?> upload(@RequestPart("postId") Long postId, @RequestPart("file") MultipartFile file,
                                 HttpServletRequest request
    ) throws IOException {

        return fileService.upload(postId,file,request);
    }

}
