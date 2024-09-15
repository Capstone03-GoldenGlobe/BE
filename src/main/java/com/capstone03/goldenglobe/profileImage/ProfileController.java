package com.capstone03.goldenglobe.profileImage;

import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // Presigned URL 생성
    @PostMapping("/users/profile/url")
    public ResponseEntity<ProfileDto> updateProfile(Authentication auth){
        ProfileDto response = profileService.getPresignedUrl(auth);
        return ResponseEntity.ok(response);
    }

    // 이미지 조회
    @GetMapping("/users/profile")
    public ResponseEntity<String> getProfile(Authentication auth){
        String profileUrl = profileService.getProfileUrl(auth);
        return ResponseEntity.ok(profileUrl);
    }

    // S3 직접 업로드
    @PostMapping("/users/profile/image")
    public ResponseEntity<?> uploadProfileImage(Authentication auth,
                                                     @RequestParam("file") MultipartFile file) {
        try {
            ProfileDto response = profileService.uploadProfileImage(auth, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("업로드 실패");
        }
    }
}
