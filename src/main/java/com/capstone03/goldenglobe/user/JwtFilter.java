package com.capstone03.goldenglobe.user;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter { // 요청마다 1회만 실행되도록 extends

    @Autowired
    private UserService userService; // 블랙리스트 검사를 위한 서비스 주입

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. Authorization 헤더에서 JWT 추출 => 없으면 쿠키에서 추출
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        } else { // 쿠키에서 JWT 추출
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

        // JWT 토큰이 없으면 필터 통과
        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 블랙리스트에 있는지 확인
        if (userService.isTokenBlacklisted(jwtToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("블랙리스트에 등록된 토큰입니다.");
            return; // 더 이상 진행하지 않고 종료
        }

        // 3. 유효기간, 위조여부 확인
        Claims claims;
        try {
            claims = JwtUtil.extractToken(jwtToken);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Claims: " + claims);

        // 4. 문제없으면 auth 변수에 유저정보 입력
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

        var authToken = new UsernamePasswordAuthenticationToken(customUser, null, authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken); // 유저정보 추가

        filterChain.doFilter(request, response); // 다음 필터 실행
    }

}


