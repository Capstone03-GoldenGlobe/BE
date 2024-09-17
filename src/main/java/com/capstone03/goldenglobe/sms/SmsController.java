package com.capstone03.goldenglobe.sms;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
@Tag(name="Sms",description = "휴대폰 인증 관련 API")
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send")
    @Operation(summary = "인증번호 전송",description = "전달받은 휴대폰 번호로 인증번호를 전송합니다.")
    public ResponseEntity<Map<String, Object>> SendSMS(@RequestBody @Valid SmsDto smsDto){
        String certificationCode = smsService.SendSms(smsDto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("phoneNumber", smsDto.getCellPhone());
        response.put("code", certificationCode);
        response.put("message", "문자 전송 완료");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    @Operation(summary = "인증번호 확인",description = "휴대폰 번호와 인증번호가 맞는지 확인합니다.")
    public ResponseEntity<String> verifySMS(@RequestBody @Valid SmsVerifyDto smsVerifyDto){
        boolean verify = smsService.verifyCode(smsVerifyDto);
        if (verify) {
            return ResponseEntity.ok("인증 완료 되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증에 실패했습니다.");
        }
    }
}
