package com.capstone03.goldenglobe.user.blackList;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@RedisHash("blacklistToken")
public class BlackList {
    @Id
    private String token; // 블랙리스트에 추가할 액세스 토큰
    private Date expirationDate; // 만료 시간
}
