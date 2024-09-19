package com.capstone03.goldenglobe.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponseDto {
    private String phoneNumber;
    private String certificationCode;
}

