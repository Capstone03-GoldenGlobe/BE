package com.capstone03.goldenglobe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseSetting<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiResponseSetting<String> errorResponse = new ApiResponseSetting<>(400, "클라이언트 오류: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponseSetting<String>> handleGeneralException(ResponseStatusException e) {
        HttpStatus status = (HttpStatus) e.getStatusCode();
        String message = e.getReason();
        ApiResponseSetting<String> errorResponse = new ApiResponseSetting<>(
                String.valueOf(status).length() >= 3 ? Integer.parseInt(String.valueOf(status).substring(0, 3)) : 500, // 상태 코드
                message != null && String.valueOf(status).length() >= 3 ? String.valueOf(status).substring(4) + ":" + message : message // 상태 코드 메시지와 원래 메시지 결합
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponseSetting<String>> handleNullPointerException(NullPointerException e) {
        // auth가 null일 때 예외 처리
        if (e.getMessage().contains("org.springframework.security.core.Authentication.getPrincipal()")) {
            ApiResponseSetting<String> response = new ApiResponseSetting<>(401, "인증되지 않은 사용자입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        ApiResponseSetting<String> response = new ApiResponseSetting<>(Integer.parseInt(e.getMessage().substring(0, 3)), e.getMessage().substring(4));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}