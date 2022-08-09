package com.example.intermediate.service;

import com.example.intermediate.controller.request.PostRequestDto;
import com.example.intermediate.controller.response.*;
import com.example.intermediate.domain.*;
import com.example.intermediate.domain.Like.CommentLike;
import com.example.intermediate.domain.Like.PostLike;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.FileRepository;
import com.example.intermediate.repository.PostRepository;
import com.example.intermediate.repository.ReCommentRepository;
import com.example.intermediate.repository.like.CommentLikeRepository;
import com.example.intermediate.repository.like.PostLikeRepository;
import com.example.intermediate.repository.like.ReCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final TokenProvider tokenProvider;
  private final CommentLikeRepository commentLikeRepository;

  private final ReCommentRepository reCommentRepository;
  private final PostLikeRepository postLikeRepository;
  private  final ReCommentLikeRepository reCommentLikeRepository;

  @Transactional
  public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request) {
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
    if(requestDto.getTitle()==null){return ResponseDto.fail("TITLE_EMPTY", "제목 칸이 비었습니다.");
    }
    if(requestDto.getContent()==null){return ResponseDto.fail("CONTENT_EMPTY", "작성된 글이 없습니다.");
    }


    Post post = Post.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .member(member)
            .imageUrl(requestDto.getImageUrl())
            .build();
    postRepository.save(post);
    return ResponseDto.success(
            PostResponseDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .author(post.getMember().getNickname())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .imageUrl(post.getImageUrl())
                    .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getPost(Long id) {
    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    List<Comment> commentList = commentRepository.findAllByPost(post);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    for (Comment comment : commentList) {
      List<CommentLike> commentLikeList = commentLikeRepository.findByComment(comment);
      int likeCount = commentLikeList.size();
      List<ReComment> reCommentListTemp = reCommentRepository.findAllByComment(comment);
      int reCommentCount = reCommentListTemp.size();
      List <ReCommentAllResponseDto> reCommentAllList =new ArrayList<>();
      for (ReComment value : reCommentListTemp) {
        Long reCommentId = value.getId();
        ReComment reComment = isPresentReComment(reCommentId);
        if(reComment==null){
          return ResponseDto.fail("RE_COMMENT_NOT_FOUND",
                  "댓글이 존재하지 않습니다.");}
        int reLikeCount = reCommentLikeRepository.findByReComment(reComment).size();
        reCommentAllList.add(
                ReCommentAllResponseDto.builder()
                        .id(reComment.getId())
                        .author(reComment.getMember().getNickname())
                        .reContent(reComment.getReContent())
                        .createdAt(reComment.getCreatedAt())
                        .modifiedAt(reComment.getModifiedAt())
                        .reLikeCount(reLikeCount)
                        .build()
        );
      }
      commentResponseDtoList.add(
              CommentResponseDto.builder()
                      .id(comment.getId())
                      .author(comment.getMember().getNickname())
                      .content(comment.getContent())
                      .createdAt(comment.getCreatedAt())
                      .modifiedAt(comment.getModifiedAt())
                      .likeCount(likeCount)
                      .reCommentCount(reCommentCount)
                      .reCommentResponseDtoList(reCommentAllList)
                      .build()
      );
    }
    String url = getImageUrlByPost(post);

    List<PostLike> postLikeList = postLikeRepository.findAllByPost(post);
    int likeCount= postLikeList.size();
    int commentCount = commentList.size();

    return ResponseDto.success(
            PostResponseDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .commentResponseDtoList(commentResponseDtoList)
                    .author(post.getMember().getNickname())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .imageUrl(url)
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .build()
    );
  }
  @Transactional(readOnly = true)
  public ReComment isPresentReComment(Long id) {
    Optional<ReComment> optionalReComment =reCommentRepository.findById(id);
    return optionalReComment.orElse(null);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllPost() {
    List<PostAllResponseDto> postAllList= new ArrayList<>();
    List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
    for (Post post : postList) {
      List<PostLike> postLikeList = postLikeRepository.findAllByPost(post);
      int likeCount = postLikeList.size();
      String url = getImageUrlByPost(post);
      int commentCount = commentRepository.findAllByPost(post).size();
      postAllList.add(
              PostAllResponseDto.builder()
                      .id(post.getId())
                      .title(post.getTitle())
                      .author(post.getMember().getNickname())
                      .createdAt(post.getCreatedAt())
                      .modifiedAt(post.getModifiedAt())
                      .imageUrl(url)
                      .likeCount(likeCount)
                      .commentCount(commentCount)
                      .build()
      );
    }
    return ResponseDto.success(postAllList);
  }


  @Transactional
  public ResponseDto<Post> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
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

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
    }

    post.update(requestDto);
    return ResponseDto.success(post);
  }

  @Transactional
  public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
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

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
    }

    
    postRepository.delete(post);
    return ResponseDto.success("delete success");
  }

  @Transactional(readOnly = true)
  public Post isPresentPost(Long id) {
    Optional<Post> optionalPost = postRepository.findById(id);
    return optionalPost.orElse(null);
  }

  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }

  public String getImageUrlByPost(Post post) {
    return post.getImageUrl();
  }


  @Transactional(readOnly = true)
  public List<PostResponseDto> getAllPostByMember(Member member) {
    List<Post> postList = postRepository.findAllByMember(member);
    List<PostResponseDto> postResponseDtoList = new ArrayList<>();

    for(Post post : postList) {
      String url = getImageUrlByPost(post);
      List<PostLike> postLikeList = postLikeRepository.findAllByPost(post);
      int likeCount= postLikeList.size();
      postResponseDtoList.add(
      PostResponseDto.builder()
              .id(post.getId())
              .title(post.getTitle())
              .author(post.getMember().getNickname())
              .content(post.getContent())
              .createdAt(post.getCreatedAt())
              .modifiedAt(post.getModifiedAt())
              .likeCount(likeCount)
              .imageUrl(url)
              .build()
      );
    }
    return postResponseDtoList;
  }

}
