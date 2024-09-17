package com.capstone03.goldenglobe.profileImage;

import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "ProfileImage",description = "유저 프로필 이미지 관련 API")
public class ProfileController {

    private final ProfileService profileService;

    // Presigned URL 생성
    @PostMapping("/users/profile/url")
    @Operation(summary = "PresignedURL 생성",description = "PresignedURL과 해당 URL을 통해 정상적으로 이미지 업로드 시, 이미지를 조회할 수 있는 URL을 반환합니다.")
    public ResponseEntity<ProfileDto> updateProfile(Authentication auth){
        ProfileDto response = profileService.getPresignedUrl(auth);
        return ResponseEntity.ok(response);
    }

    // 이미지 조회
    @GetMapping("/users/profile")
    @Operation(summary = "이미지 조회 URL 생성",description = "로그인한 유저의 프로필 이미지 URL정보를 가져옵니다.")
    public ResponseEntity<String> getProfile(Authentication auth){
        String profileUrl = profileService.getProfileUrl(auth);
        return ResponseEntity.ok(profileUrl);
    }

    // S3 직접 업로드
    @PostMapping("/users/profile/image")
    @Operation(summary = "서버에서 이미지 직접 업로드",description = "서버가 프론트에서 이미지를 받아 S3 bucket에 이미지를 업로드합니다.")
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
