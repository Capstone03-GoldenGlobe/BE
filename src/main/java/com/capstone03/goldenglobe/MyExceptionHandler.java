package com.capstone03.goldenglobe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiResponse<String> errorResponse = new ApiResponse<>(400, "클라이언트 오류: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception e) {
        ApiResponse<String> errorResponse = new ApiResponse<>(500, "서버 내부 오류: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<String>> handleNullPointerException(NullPointerException e) {
        // auth가 null일 때 예외 처리
        if (e.getMessage().contains("org.springframework.security.core.Authentication.getPrincipal()")) {
            ApiResponse<String> response = new ApiResponse<>(401, "인증되지 않은 사용자입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // 다른 이유로 null이 뜰 경우
        ApiResponse<String> response = new ApiResponse<>(500, "서버 내부 오류: "+e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}