package com.capstone03.goldenglobe.sms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsVerifyDto {
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String cellPhone;

    @NotBlank(message = "인증번호를 입력해주세요.")
    private String certificationCode;
}