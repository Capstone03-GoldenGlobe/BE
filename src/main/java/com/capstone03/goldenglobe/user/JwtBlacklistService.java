package com.capstone03.goldenglobe.user;

import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtBlacklistService {

  private final Set<String> blacklistedTokens = new HashSet<>();

  // 블랙리스트에 토큰 추가
  public void addToBlacklist(String token) {
    blacklistedTokens.add(token);
  }

  // 블랙리스트에서 토큰 확인
  public boolean isTokenBlacklisted(String token) {
    return blacklistedTokens.contains(token);
  }
}
