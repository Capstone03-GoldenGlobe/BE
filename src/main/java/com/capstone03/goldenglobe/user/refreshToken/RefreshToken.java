package com.capstone03.goldenglobe.user.refreshToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@RedisHash(value = "jwtToken", timeToLive = 60*60*24*3) // 3일
public class RefreshToken {
    @Id
    private Long id;

    private String refreshToken;
}
