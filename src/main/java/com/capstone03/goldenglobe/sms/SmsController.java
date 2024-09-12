package com.capstone03.goldenglobe.sms;

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
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send")
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
    public ResponseEntity<String> verifySMS(@RequestBody @Valid SmsVerifyDto smsVerifyDto){
        boolean verify = smsService.verifyCode(smsVerifyDto);
        if (verify) {
            return ResponseEntity.ok("인증 완료 되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증에 실패했습니다.");
        }
    }
}
