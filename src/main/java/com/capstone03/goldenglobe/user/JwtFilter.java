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
        // 1. Authorization 헤더에서 JWT 추출 => 없으면 쿠키에서 추출 (for swagger)
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        } else { //쿠키에서 JWT 추출
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwtToken = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // 2. 유효기간, 위조여부 확인해보고
        Claims claims;
        try{ // 에러가 날 수 있으므로 try, catch 안에 써줌
            claims = JwtUtil.extractToken(jwtToken);
        } catch (Exception e) {
            filterChain.doFilter(request, response); // 다음필터실행
            return;
        }

        System.out.println("Claims: " + claims);

        // 3. 문제없으면 auth 변수에 유저정보 입력
        var authorities = Arrays.stream(claims.get("authorities").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        var customUser = new CustomUser(
                claims.get("cellphone").toString(),
                "none",
                authorities
        );
        customUser.setId(((Number) claims.get("id")).longValue()); // id 설정
        customUser.setName(claims.get("name").toString()); // name 설정

        var authToken = new UsernamePasswordAuthenticationToken(
                customUser, null, authorities
        );
        authToken.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request)); //auth 변수를 좀 더 잘 쓸 수 있게 만들어줌
        SecurityContextHolder.getContext().setAuthentication(authToken); //auth 변수를 좀 더 잘 쓸 수 있게 만들어줌

        filterChain.doFilter(request, response);
    }
}