package com.capstone03.goldenglobe.profileImage;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;

    public String getPresignedUrl(Authentication auth){
        CustomUser customUser = (CustomUser) auth.getPrincipal();

        String fileName = "profile/"+customUser.getId()+".jpg";

        // url 만료시간 설정
        Date expiration = new Date();
        long expTime = expiration.getTime();
        expTime += 1000*60*5 ; // 5분 후 만료
        expiration.setTime(expTime);

        // presigned URL 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket,fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        String presignedUrl = String.valueOf(amazonS3.generatePresignedUrl(generatePresignedUrlRequest));

        Optional<User> user = userRepository.findById(customUser.getId());

        user.get().setProfile(fileName);
        userRepository.save(user.get());
        return presignedUrl;
    }

    public String getProfileUrl(Authentication auth){
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        Optional<User> user = userRepository.findById(customUser.getId());
        return amazonS3.getUrl(bucket,user.get().getProfile()).toString();
    }

    public void uploadProfileImage(Authentication auth, MultipartFile file) throws IOException {
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        String fileName = "profile/" + customUser.getId() + ".jpg";

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

        Optional<User> user = userRepository.findById(customUser.getId());
        user.get().setProfile(fileName);
        userRepository.save(user.get());
    }
}
