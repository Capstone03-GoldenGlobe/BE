package com.capstone03.goldenglobe.PdfList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
  @Value("${file.upload-dir}")
  private String uploadDir;

  public void storeFile(MultipartFile file) throws IOException {
    Path uploadPath = Paths.get(uploadDir);

    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    Path filePath = uploadPath.resolve(file.getOriginalFilename());
    Files.copy(file.getInputStream(), filePath);
  }

  public void deleteFile(String filename) throws IOException {
    Path filePath = Paths.get(uploadDir).resolve(filename);
    Files.deleteIfExists(filePath);
  }
}