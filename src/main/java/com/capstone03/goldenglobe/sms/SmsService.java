package com.capstone03.goldenglobe.sms;

import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsUtil smsUtil;
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate; // Redis 사용
    private static final long EXPIRATION_TIME = 5L; // 유효시간 5분

    public String SendSms(SmsDto smsDto){
        // 이미 가입한 전화번호인지 확인
        String to = smsDto.getCellPhone();
        boolean isExists = userRepository.findByCellphone(to).isPresent();
        if (isExists){ // 이미 가입한 번호라면
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 회원가입된 번호입니다.");
        }
        // 인증번호 생성
        String certificationCode = String.format("%06d", (int)(Math.random() * 1000000)); // 6자리 인증코드

        // 발송 정보 redis에 저장
        redisTemplate.opsForValue().set(to, certificationCode, EXPIRATION_TIME, TimeUnit.MINUTES);

        // 문자 발송
        smsUtil.sendOne(to,certificationCode);

        return certificationCode;
    }

    public boolean verifyCode(SmsVerifyDto smsVerifyDto){
        String to = smsVerifyDto.getCellPhone();
        String inputCode = smsVerifyDto.getCertificationCode();

        // Redis에서 저장된 인증번호 가져오기
        String storedCode = redisTemplate.opsForValue().get(to);

        if(storedCode == null){ // 저장된 코드가 없을 겨우
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"DB에서 인증번호를 찾을 수 없습니다.");
        } else if (!storedCode.equals(inputCode)){ // 인증번호가 일치하지 않을 경우
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"인증번호가 일치하지 않습니다.");
        }

        // 인증 성공 시 Redis에서 인증번호 삭제 (optional)
        redisTemplate.delete(to);
        return true;
    }
}
