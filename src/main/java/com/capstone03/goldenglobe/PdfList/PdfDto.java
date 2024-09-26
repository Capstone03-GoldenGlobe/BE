package com.capstone03.goldenglobe.PdfList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfDto {
  private String presignedUrl; // Presigned URL (옵션)
  private String pdfUrl;       // 업로드된 PDF 파일의 S3 URL
  private String fileName;     // PDF 파일 이름
}
