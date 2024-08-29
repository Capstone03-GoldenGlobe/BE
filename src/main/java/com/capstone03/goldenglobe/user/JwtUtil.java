package com.capstone03.goldenglobe.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    static final SecretKey key =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(
            // application.properties에 넣어서 꺼내쓰도록 변경 필요
            "thiskeyisneededatlist32textandneedtouseapplicationproperties"
        ));

    // JWT 액세스 토큰 생성 함수
    public static String createToken(Authentication auth) {
        var user = (CustomUser) auth.getPrincipal();
        var authorities = auth.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.joining(","));
        return Jwts.builder()
            .claim("name", user.getName())
            .claim("id", user.getId())
            .claim("authorities", authorities)
            .claim("email", user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1800000)) // 30분
            .signWith(key)
            .compact();
    }

    // JWT 리프레시 토큰 생성 함수
    public static String createRefreshToken(Authentication auth) {
        var user = (CustomUser) auth.getPrincipal();
        return Jwts.builder()
            .claim("id", user.getId()) // 최소한의 정보만 포함
            .claim("email", user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일
            .signWith(key)
            .compact();
    }

    // JWT 토큰에서 클레임 추출하는 함수
    public static Claims extractToken(String token) {
        return Jwts.parser()  // `parser()` 메서드를 사용합니다.
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)  // 이 메서드는 `JwtParser` 객체에서 호출됩니다.
            .getBody();
    }
}