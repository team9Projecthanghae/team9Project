package com.example.intermediate.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.intermediate.domain.File;
import com.example.intermediate.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@PropertySource("classpath:application-s3.properties")
@Slf4j
@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final FileRepository fileRepository;
    private final AmazonS3Client amazonS3Client;

    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "*/10 * * * * *")
    public void deleteImages() throws InterruptedException {
        List<File> fileList = fileRepository.findAll();
        String imageList = amazonS3Client.getObjectAsString(bucket, "${cloud.aws.credentials.accessKey}");
        log.info(imageList);
        System.out.println(imageList);
        log.info("바보");
        System.out.println("바보");

    }
}
