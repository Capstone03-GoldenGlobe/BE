package com.capstone03.goldenglobe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PdfList {
  @Id
  @Column(name = "pdf_id", nullable = false, length = 50)
  private String pdfId;

  @Column(name = "user_id", nullable = false, length = 50)
  private String userId;

  @Column(name = "dest_id", nullable = false, length = 50)
  private String destId;

  @Column(name = "pdf_path")
  private String pdfPath;
}
