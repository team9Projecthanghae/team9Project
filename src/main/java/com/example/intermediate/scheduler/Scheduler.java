package com.example.intermediate.scheduler;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.intermediate.domain.Post;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.FileRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final FileRepository fileRepository;

    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 1 * * *")
    public void deleteimage() throws InterruptedException {
        System.out.println("사진 삭제 실행");
        List<Post> postList = postRepository.findAll();
        List<Comment> commentList = commentRepository.findAll);

        for (Post post : postList) {





            }
        }
    }
}