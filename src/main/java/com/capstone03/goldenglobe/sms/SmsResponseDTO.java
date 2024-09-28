package com.capstone03.goldenglobe.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponseDTO {
    private String phoneNumber;
    private String certificationCode;
}

