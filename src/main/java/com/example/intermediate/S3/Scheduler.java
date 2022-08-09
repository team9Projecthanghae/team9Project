package com.example.intermediate.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.intermediate.domain.File;
import com.example.intermediate.domain.Post;
import com.example.intermediate.repository.FileRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@PropertySource("classpath:application-s3.properties")
@Slf4j
@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final PostRepository postRepository;
    private final AmazonS3Client amazonS3Client;

    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 */1 * * * *")
    public void deleteImages() {
        System.out.println("****************************************************************");
        System.out.println("사진 정리를 시작합니다");
        List<Post>postList =postRepository.findAll();
        ObjectListing uploadedImageList = amazonS3Client.listObjects(bucket);
        List<S3ObjectSummary> imageList=uploadedImageList.getObjectSummaries();
        for(S3ObjectSummary s3ObjectSummaries:imageList){
            int used=0;
            for(Post post:postList){
                if(Objects.equals(s3ObjectSummaries.getKey(), post.getImageUrl().substring(56))){
                    used+=1;
                    System.out.println(post.getImageUrl().substring(56));
                    System.out.println(s3ObjectSummaries.getKey());
                }
            }
            if (used<1){
                System.out.println("***************warning************");
                System.out.println(s3ObjectSummaries.getKey());
                System.out.println("사진이 삭제되었습니다");
//                amazonS3Client.deleteObject(bucket,s3ObjectSummaries.getKey());
            }
        }
        System.out.println("사진 정리를 종료합니다");
        System.out.println("****************************************************************");
    }
}
