package com.capstone03.goldenglobe.sms;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    @Value("${coolsms.from.number}")
    private String fromNumber;

    private DefaultMessageService messageService;

    // 초기 설정
    @PostConstruct
    private void init(){
        messageService = NurigoApp.INSTANCE.initialize(
                apiKey,
                apiSecretKey,
                "https://api.coolsms.co.kr"
        );
    }

    // 인증번호 발송
    public SingleMessageSentResponse sendOne(String to, String verificationCode) {
        Message message = new Message();
        message.setFrom(fromNumber); // 발신번호
        message.setTo(to); // 수신번호
        message.setText("[GoldenGlobe]인증번호 : " + verificationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }
}