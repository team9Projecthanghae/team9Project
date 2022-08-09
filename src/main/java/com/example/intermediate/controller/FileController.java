package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final String string = "/api/auth/image";

    @PostMapping(string)
    public ResponseDto<?> upload(@RequestPart("file") MultipartFile file,
                                 HttpServletRequest request
    ) throws IOException {
        return fileService.upload(file,request);
    }

}
