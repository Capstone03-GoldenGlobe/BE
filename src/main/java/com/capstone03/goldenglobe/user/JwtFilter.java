package com.capstone03.goldenglobe.user;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter { //요청마다 1회만 실행되도록 extends

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. jwt 이름의 쿠키가 있으면 꺼내서
        Cookie[] cookies = request.getCookies();
        if(cookies==null){ // 통과하기
            filterChain.doFilter(request, response); // 다음필터실행
            return;
        }
        var jwtCookie = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                jwtCookie = cookie.getValue(); // jwt 쿠키를 가져옴
            }
        }

        // 2. 유효기간, 위조여부 확인해보고
        Claims claim;
        try{ // 에러가 날 수 있으므로 try, catch 안에 써줌
            claim = JwtUtil.extractToken(jwtCookie);
        } catch (Exception e) {
            filterChain.doFilter(request, response); // 다음필터실행
            return;
        }

        System.out.println("Claims: " + claim);

        // 3. 문제없으면 auth 변수에 유저정보 입력
        var arr = claim.get("authorities").toString().split(","); // 권한들을 list에 담아서 넘겨 줌
        var authorities = Arrays.stream(arr).map(a -> new SimpleGrantedAuthority(a)).toList(); // 권한들은 SimpleGrantedAuthority안에 넣어야 함. 넣고 toList()로 리스트 변환

        var customUser = new CustomUser(
                claim.get("email").toString(),
                "none",
                authorities
        );
        customUser.setId(((Number) claim.get("id")).longValue()); // id 설정
        customUser.setName(claim.get("name").toString()); // name 설정
//        customUser.setEmail(claim.get("email").toString());

        var authToken = new UsernamePasswordAuthenticationToken(
                customUser, null, authorities
        );
        authToken.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request)); //auth 변수를 좀 더 잘 쓸 수 있게 만들어줌
        SecurityContextHolder.getContext().setAuthentication(authToken); //auth 변수에 맘대로 유저정보 추가 가능

        filterChain.doFilter(request, response);
    }
}

