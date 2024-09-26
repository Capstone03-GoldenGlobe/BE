package com.capstone03.goldenglobe.PdfList;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfService {
  @Value("${aws.s3.pdf-bucket}")
  private String bucket;
  private final AmazonS3 amazonS3;
  private final UserRepository userRepository;

  public String getPresignedUrl(Authentication auth) {
    CustomUser customUser = (CustomUser) auth.getPrincipal();
    String fileName = "pdfs/" + customUser.getId() + "_" + UUID.randomUUID().toString() + ".pdf";

    // URL 만료 시간 설정
    Date expiration = new Date();
    expiration.setTime(expiration.getTime() + 1000 * 60 * 5); // 5분 후 만료

    // Presigned URL 생성
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expiration);

    return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
  }

  public String uploadPdf(Authentication auth, MultipartFile file) throws IOException {
    CustomUser customUser = (CustomUser) auth.getPrincipal();
    String fileName = "pdfs/" + customUser.getId() + "_" + UUID.randomUUID().toString() + ".pdf";

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(file.getContentType());
    metadata.setContentLength(file.getSize());

    // 파일 업로드
    amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

    // 사용자의 PDF 경로를 업데이트
    Optional<User> user = userRepository.findById(customUser.getId());
    if (user.isPresent()) {
      userRepository.save(user.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
    }

    return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
  }
}
