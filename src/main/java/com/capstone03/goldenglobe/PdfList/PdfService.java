package com.capstone03.goldenglobe.PdfList;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PdfService {
  @Value("${aws.s3.pdf-bucket}")
  private String bucket;
  private final AmazonS3 amazonS3;
  private final UserRepository userRepository;

  private final PdfListRepository pdfListRepository;

  public String getPresignedUrl(PdfUploadDTO request, Authentication auth) {
    CustomUser customUser = (CustomUser) auth.getPrincipal();
    Long destId = request.getDestId();
    String fileName = request.getFileName();

    // destId를 가진 pdf_list의 개수가 총 몇개인지
    int count = pdfListRepository.countByDestId(destId);
    count = count+1;

    // 보안을 위해 파일명을 추후 hash로 바꾸기
    String filePath = "pdfs/" + destId + "_" + (count) + "_" + fileName;

    // URL 만료 시간 설정
    Date expiration = new Date();
    expiration.setTime(expiration.getTime() + 1000 * 60 * 5); // 5분 후 만료

    // Presigned URL 생성
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucket, filePath)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expiration);

    // DB 저장
    PdfList pdfList = new PdfList();
    pdfList.setUserId(customUser.getId());
    pdfList.setDestId(destId);
    pdfList.setPdfPath(filePath);
    pdfList.setPdfName(fileName);
    pdfListRepository.save(pdfList);

    return String.valueOf(amazonS3.generatePresignedUrl(generatePresignedUrlRequest));
  }

  public List<PdfInfoDTO> getPdfInfos(Long destId, Authentication auth){
    List<String> pdfInfo = pdfListRepository.findAllPdfNamesByDestId(destId);
    List<PdfInfoDTO> pdfInfoDto = new ArrayList<>();

    for (String pdfData : pdfInfo){
      String[] parts = pdfData.split(",");
      Long pdfId = Long.parseLong(parts[0]);
      String pdfName = parts[1];
      String pdfPaths = amazonS3.getUrl(bucket, parts[2]).toString();
      pdfInfoDto.add(new PdfInfoDTO(pdfId, pdfName, pdfPaths));
    }
    return pdfInfoDto;
  }

  public List<String> getPdfPaths(Long destId, Authentication auth){
    List<String> pdfPaths = pdfListRepository.findAllPdfPathsByDestId(destId);

    List<String> s3Paths = new ArrayList<>();
    for (String pdfPath : pdfPaths) {
      // 각 PDF 경로를 S3 URL로 변환
      String s3Path = amazonS3.getUrl(bucket, pdfPath).toString();
      s3Paths.add(s3Path);
    }

    // 변환된 S3 URL 리스트를 반환합니다.
    return s3Paths;
  }

  public String uploadPdf(Authentication auth, MultipartFile file, Long destId) throws IOException {
    CustomUser customUser = (CustomUser) auth.getPrincipal();

    String fileName = file.getOriginalFilename();

    // destId를 가진 pdf_list의 개수가 총 몇개인지
    int count = pdfListRepository.countByDestId(destId);
    count = count+1;

    // 보안을 위해 파일명을 추후 hash로 바꾸기
    String filePath = "pdfs/" + destId + "_" + (count) + "_" + fileName;

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(file.getContentType());

    // 파일 확장자가 PDF인지 확인하는 로직
    if (!fileName.toLowerCase().endsWith(".pdf") || !"application/pdf".equals(file.getContentType())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pdf파일만 업로드 가능합니다.");
    }

    // 파일 업로드
    amazonS3.putObject(bucket, filePath, file.getInputStream(), metadata);

//    // Flask API를 통해 PDF 임베딩 수행
//    String vectorStoreId;
//    try {
//      vectorStoreId = callFlaskEmbedApi(file);
//    } catch (Exception e) {
//      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "PDF 임베딩 중 오류 발생: " + e.getMessage());
//    }


    // DB 저장
    PdfList pdfList = new PdfList();
    pdfList.setUserId(customUser.getId());
    pdfList.setDestId(destId);
    pdfList.setPdfPath(filePath);
    pdfList.setPdfName(fileName);
    //pdfList.setPdfContent(vectorStoreId);
    pdfListRepository.save(pdfList);

    return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + filePath;
  }

  public void deletePdf(Long pdfId, Authentication auth){
    PdfList pdfList = pdfListRepository.findById(pdfId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PDF를 찾을 수 없습니다."));
    // bucket 에서 삭제
    //amazonS3.deleteObject(bucket,pdfList.getPdfPath());
    try {
      amazonS3.deleteObject(bucket, pdfList.getPdfPath());
    } catch (AmazonServiceException e) {
      // AWS 서비스 오류 처리
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "S3에서 PDF 삭제 실패: " + e.getMessage());
    } catch (SdkClientException e) {
      // 클라이언트 오류 처리
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "S3 클라이언트 오류: " + e.getMessage());
    }
    // db 삭제
    pdfListRepository.deleteById(pdfId);
  }
}
