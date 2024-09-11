package com.capstone03.goldenglobe.sms;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> SendSMS(@RequestParam("phoneNumber") String phoneNumber){
        String certificationCode = smsService.SendSms(phoneNumber);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("phoneNumber", phoneNumber);
        response.put("code", certificationCode);
        response.put("message", "문자 전송 완료");
        return ResponseEntity.ok(response);
    }

    //@PostMapping("/verify")
}
