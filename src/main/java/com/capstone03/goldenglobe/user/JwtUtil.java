package com.capstone03.goldenglobe.user;

import com.capstone03.goldenglobe.user.CustomUser;
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

    // JWT 만들어주는 함수
    public static String createToken(Authentication auth){
        var user = (CustomUser) auth.getPrincipal(); //auth는 타입캐스팅해서 써야함
        // claim 값으로 리스트 못 집어넣기 때문에 권한을 문자로 만들어서(.getAuthority()) 붙이기(joining(","))
        var authorities = auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));
        String jwt = Jwts.builder() // jwt에 있는 정보들은 누구나 볼 수 있음
                .claim("name", user.getName()) // name 클레임 추가
                .claim("id", user.getId()) // id 클레임 추가
                .claim("authorities",authorities)
                .claim("email",user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1800000 )) // ms 단위 : 유효기간 30분
                .signWith(key) // 해싱할 때 넣을 비번
                .compact();
        return jwt;
    }

    // JWT token 확인하는 함수
    public static Claims extractToken(String token){
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        return claims; // jwt에 들어있는 데이터들
    }
}

