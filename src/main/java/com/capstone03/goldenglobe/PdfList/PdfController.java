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
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pdf", description = "PDF 관련 API")
public class PdfController {

  private final PdfService pdfService;

  // 챗봇에 presignedUrl 생성 및 파일명 db 저장
  @PostMapping("/pdf")
  @Operation(summary = "PresignedURL 생성", description = "PresignedURL과 해당 URL을 통해 정상적으로 PDF 업로드 시, PDF를 조회할 수 있는 URL을 반환합니다.")
  public ResponseEntity<ApiResponseSetting<String>> getPresignedUrl(@RequestBody PdfUploadDTO request, Authentication auth) {
    // 챗봇 접근 권한 체크 필요
    String presignedUrl = pdfService.getPresignedUrl(request, auth);
    ApiResponseSetting<String> response = new ApiResponseSetting<>(HttpStatus.OK.value(), "Presigned URL 생성 완료", presignedUrl);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/pdf/infos/{dest_id}") // 좌측 nav bar에 보여주기 위한 API
  @Operation(summary = "챗봇 PDF 이름 목록 조회", description = "dest_id와 일치하는 모든 PDF URL 반환")
  public ResponseEntity<ApiResponseSetting<List<PdfInfoDTO>>> getAllPdfInfos(@PathVariable("dest_id") Long destId, Authentication auth){
    // 챗봇 접근 권한 체크 필요
    List<PdfInfoDTO> pdfInfos = pdfService.getPdfInfos(destId,auth);
    ApiResponseSetting<List<PdfInfoDTO>> response = new ApiResponseSetting<>(HttpStatus.OK.value(), "챗봇 PDF 목록(이름) 조회 완료", pdfInfos);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/pdf/paths/{dest_id}") // 챗봇을 위한 API
  @Operation(summary = "챗봇 PDF URL 목록 조회", description = "dest_id와 일치하는 모든 PDF URL 반환")
  public ResponseEntity<ApiResponseSetting<List<String>>> getAllPdfPaths(@PathVariable("dest_id") Long destId, Authentication auth){
    // 챗봇 접근 권한 체크 필요
    List<String> pdfPaths = pdfService.getPdfPaths(destId,auth);
    ApiResponseSetting<List<String>> response = new ApiResponseSetting<>(HttpStatus.OK.value(), "챗봇 PDF 목록(url) 조회 완료", pdfPaths);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/pdf/{pdf_id}")
  @Operation(summary = "PDF 삭제",description = "pdf_id와 일치하는 PDF 삭제")
  public ResponseEntity<ApiResponseSetting<Void>> deletePdf(@PathVariable("pdf_id") Long pdfId, Authentication auth){
    // 챗봇 접근 권한 체크 필요
    pdfService.deletePdf(pdfId,auth);
    ApiResponseSetting<Void> response = new ApiResponseSetting<>(200, "Pdf 삭제 완료", null);
    return ResponseEntity.ok(response);
  }

  // PDF 직접 업로드
  @PostMapping("/pdf/{dest_id}")
  @Operation(summary = "서버에서 PDF 직접 업로드", description = "서버가 프론트에서 PDF 파일을 받아 S3 bucket에 PDF를 업로드합니다.")
  public ResponseEntity<ApiResponseSetting<String>> uploadPdf(Authentication auth,
                                                              @RequestParam("file") MultipartFile file, @PathVariable("dest_id") Long destId) {
    // 챗봇 접근 권한 체크 필요
    try {
      String pdfUrl = pdfService.uploadPdf(auth, file, destId);
      ApiResponseSetting<String> response = new ApiResponseSetting<>(HttpStatus.OK.value(), "PDF 업로드 성공", pdfUrl);
      return ResponseEntity.ok(response);
    } catch (IOException e) {
      ApiResponseSetting<String> response = new ApiResponseSetting<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "pdf 업로드 실패", null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}
