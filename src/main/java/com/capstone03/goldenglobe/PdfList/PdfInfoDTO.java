package com.capstone03.goldenglobe.PdfList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfInfoDTO {
    private Long pdfId;
    private String pdfName;
    private String pdfPath;
}
