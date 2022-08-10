package com.example.intermediate.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.intermediate.domain.Post;
import com.example.intermediate.repository.FileRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@PropertySource("classpath:application-s3.properties")
@Slf4j
@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final AmazonS3Client amazonS3Client;

    // 초, 분, 시, 일, 월, 주 순서

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void deleteImages() {
        log.info("****************************************************************");
        log.info("사진 정리를 시작합니다");
        List<Post>postsList =postRepository.findAll();
        ObjectListing uploadedImageList = amazonS3Client.listObjects(bucket);
        List<S3ObjectSummary> imageList=uploadedImageList.getObjectSummaries();
        String BASE_URL = "https://hyeonseo-bucket.s3.ap-northeast-2.amazonaws.com/";
        for(S3ObjectSummary s3ObjectSummaries:imageList){
            int used=0;
            for(Post post:postsList){
                if(post.getImageUrl().equals(BASE_URL + s3ObjectSummaries.getKey())){
                    used+=1;
                    log.info("돌았단다 허허");
                }
            }
            if (used<1){
                log.info("***************warning************");
                log.info(s3ObjectSummaries.getKey());
                log.info("사진이 삭제되었습니다");
                fileRepository.deleteByUrl(BASE_URL + s3ObjectSummaries.getKey());
                amazonS3Client.deleteObject(bucket,s3ObjectSummaries.getKey());
            }
        }
        log.info("사진 정리를 종료합니다");
        log.info("****************************************************************");
    }

    @Scheduled(cron = "0 0 13 * * *")
    @Transactional
    public void advertisePostsWithNoComment() {
        System.out.println("****************************************************************");
        System.out.println("게시글 홍보를 시작합니다");
        List<Post> postList = postRepository.findAll();
        for (Post post : postList) {
            if (post.getComments().size() < 1) {
                System.out.println(post.getMember().getNickname() + "님이 작성한 " + post.getTitle() + "에 첫 댓글을 달아주세요!");
                System.out.println("      *      *       *       *        *         *         ");
            }
        }
        System.out.println("****************************************************************");
    }
}
