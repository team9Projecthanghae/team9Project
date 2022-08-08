package com.example.intermediate.controller;

import com.example.intermediate.controller.request.PostRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD

import javax.servlet.http.HttpServletRequest;
=======
>>>>>>> ffcb8ac5641360ccbb7c95fc4d2fee72d99b364d

@RequiredArgsConstructor
@RestController
public class PostController {

  private final PostService postService;

  @PostMapping("/api/auth/post")
  @PostMapping(value = "/api/auth/post")
  public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
      HttpServletRequest request) {
    return postService.createPost(requestDto, request);
  }

  @GetMapping("/api/post/{id}")
  @GetMapping(value = "/api/post/{id}")
  public ResponseDto<?> getPost(@PathVariable Long id) {
    return postService.getPost(id);
  }

  @GetMapping("/api/post")
  @GetMapping(value = "/api/post")
  public ResponseDto<?> getAllPosts() {
    return postService.getAllPost();
  }

  @PutMapping("/api/auth/post/{id}")
  @PutMapping(value = "/api/auth/post/{id}")
  public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
      HttpServletRequest request) {
    return postService.updatePost(id, postRequestDto, request);
  }

  @DeleteMapping("/api/auth/post/{id}")
  @DeleteMapping(value = "/api/auth/post/{id}")
  public ResponseDto<?> deletePost(@PathVariable Long id,
      HttpServletRequest request) {
    return postService.deletePost(id, request);
  }

}
