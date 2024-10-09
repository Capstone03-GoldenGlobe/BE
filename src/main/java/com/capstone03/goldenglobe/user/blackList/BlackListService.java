package com.capstone03.goldenglobe.user.blackList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class BlackListService {
    private final BlackListRepository blackListRepository;

    // 블랙리스트에 토큰 추가
    public void addToBlacklist(String token, Date expirationDate) {
        BlackList blackListToken = new BlackList(token, expirationDate);
        blackListRepository.save(blackListToken);
    }

    // 블랙리스트에서 토큰 확인
    public boolean isTokenBlacklisted(String token) {
        return blackListRepository.findById(token).isPresent();
    }
}
