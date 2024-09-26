package com.capstone03.goldenglobe.PdfList;

import com.capstone03.goldenglobe.ApiResponseSetting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pdf", description = "PDF 관련 API")
public class PdfController {

  private final PdfService pdfService;

  // Presigned URL 생성
  @PostMapping("/pdf/url")
  @Operation(summary = "PresignedURL 생성", description = "PresignedURL과 해당 URL을 통해 정상적으로 PDF 업로드 시, PDF를 조회할 수 있는 URL을 반환합니다.")
  public ResponseEntity<ApiResponseSetting<String>> getPresignedUrl(Authentication auth) {
    String presignedUrl = pdfService.getPresignedUrl(auth);
    ApiResponseSetting<String> response = new ApiResponseSetting<>(HttpStatus.OK.value(), "Presigned URL 생성 완료", presignedUrl);
    return ResponseEntity.ok(response);
  }

  // S3 직접 업로드
  @PostMapping("/pdf/upload")
  @Operation(summary = "서버에서 PDF 직접 업로드", description = "서버가 프론트에서 PDF 파일을 받아 S3 bucket에 PDF를 업로드합니다.")
  public ResponseEntity<ApiResponseSetting<String>> uploadPdf(Authentication auth,
                                                              @RequestParam("file") MultipartFile file) {
    try {
      String pdfUrl = pdfService.uploadPdf(auth, file);
      ApiResponseSetting<String> response = new ApiResponseSetting<>(HttpStatus.OK.value(), "PDF 업로드 성공", pdfUrl);
      return ResponseEntity.ok(response);
    } catch (IOException e) {
      ApiResponseSetting<String> response = new ApiResponseSetting<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "pdf 업로드 실패", null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}
