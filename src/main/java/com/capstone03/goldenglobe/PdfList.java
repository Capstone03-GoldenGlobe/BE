package com.capstone03.goldenglobe;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PdfList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pdf_id", nullable = false, length = 50)
  private String pdfId;

  @Column(name = "user_id", nullable = false, length = 50)
  private String userId;

  @Column(name = "dest_id", nullable = false, length = 50)
  private String destId;

  @Column(name = "pdf_path")
  private String pdfPath;
}
