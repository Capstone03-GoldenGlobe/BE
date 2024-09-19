package com.capstone03.goldenglobe.PdfList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/pdffiles")
public class FileController {
  @Autowired
  private FileStorageService fileStorageService;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      fileStorageService.storeFile(file);
      return ResponseEntity.ok("File uploaded successfully.");
    } catch (IOException e) {
      return ResponseEntity.status(500).body("Failed to upload file.");
    }
  }

  @DeleteMapping("/delete/{filename}")
  public ResponseEntity<String> deleteFile(@PathVariable String filename) {
    try {
      fileStorageService.deleteFile(filename);
      return ResponseEntity.ok("File deleted successfully.");
    } catch (IOException e) {
      return ResponseEntity.status(500).body("Failed to delete file.");
    }
  }
}
