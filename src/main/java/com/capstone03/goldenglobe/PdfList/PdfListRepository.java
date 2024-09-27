package com.capstone03.goldenglobe.PdfList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PdfListRepository extends JpaRepository<PdfList, Long> {
    int countByDestId(Long destId);

    @Query("SELECT p.pdfPath FROM PdfList p WHERE p.destId = ?1")
    List<String> findAllPdfPathsByDestId(Long destId);

    @Query("SELECT p.pdfName FROM PdfList p WHERE p.destId = ?1")
    List<String> findAllPdfNamesByDestId(Long destId);
}
