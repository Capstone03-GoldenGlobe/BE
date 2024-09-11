package com.capstone03.goldenglobe.sms;

import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsUtil smsUtil;
    private final UserRepository userRepository;

    public String SendSms(String to){
        // 이미 가입한 전화번호인지 확인
        boolean isExists = userRepository.findByCellphone(to).isPresent();
        if (isExists){ // 이미 가입한 번호라면
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 회원가입된 번호입니다.");
        }

        // 인증번호 생성
        String certificationCode = String.format("%06d", (int)(Math.random() * 1000000)); // 6자리 인증코드

        // 문자 발송
        smsUtil.sendOne(to,certificationCode);

        return certificationCode;
    }
}
