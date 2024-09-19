package com.capstone03.goldenglobe.sms;

import com.capstone03.goldenglobe.ApiResponseSetting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
@Tag(name="Sms",description = "휴대폰 인증 관련 API")
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send")
    @Operation(summary = "인증번호 전송",description = "전달받은 휴대폰 번호로 인증번호를 전송합니다.")
    public ResponseEntity<ApiResponseSetting<SmsResponseDto>> SendSMS(@RequestBody @Valid SmsDto smsDto){
        String certificationCode = smsService.SendSms(smsDto);
        SmsResponseDto responseDto = new SmsResponseDto(smsDto.getCellPhone(), certificationCode);
        ApiResponseSetting<SmsResponseDto> response = new ApiResponseSetting<>(200, "문자 전송 완료", responseDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    @Operation(summary = "인증번호 확인",description = "휴대폰 번호와 인증번호가 맞는지 확인합니다.")
    public ResponseEntity<ApiResponseSetting<Void>> verifySMS(@RequestBody @Valid SmsVerifyDto smsVerifyDto){
        boolean verify = smsService.verifyCode(smsVerifyDto);
        if (verify) {
            ApiResponseSetting<Void> response = new ApiResponseSetting<>(200, "인증 완료", null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponseSetting<Void> response = new ApiResponseSetting<>(400, "인증 실패", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
