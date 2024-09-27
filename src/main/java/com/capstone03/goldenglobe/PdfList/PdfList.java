package com.capstone03.goldenglobe.PdfList;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PdfList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pdf_id", nullable = false, length = 50)
  private Long pdfId;

  @Column(name = "user_id", nullable = false, length = 50)
  private Long userId;

  @Column(name = "dest_id", nullable = false, length = 50)
  private Long destId;

  @Column(name="pdf_name")
  private String pdfName;

  @Column(name = "pdf_path")
  private String pdfPath;
}
