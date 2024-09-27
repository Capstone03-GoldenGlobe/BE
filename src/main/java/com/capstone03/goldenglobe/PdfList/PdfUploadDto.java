package com.capstone03.goldenglobe.PdfList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfUploadDto {
    private Long destId;
    private String fileName; // 클라이언트에서 전달받은 이름
}
