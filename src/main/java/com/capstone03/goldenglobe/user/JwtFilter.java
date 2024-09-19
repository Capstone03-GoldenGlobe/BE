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
        // 1. jwt 이름의 쿠키가 있으면 꺼내서
        Cookie[] cookies = request.getCookies();
        if(cookies == null){ // 통과하기
            filterChain.doFilter(request, response); // 다음 필터 실행
            return;
        }

        String jwtCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                jwtCookie = cookie.getValue(); // jwt 쿠키를 가져옴
                break;
            }
        }

        if (jwtCookie == null) {
            filterChain.doFilter(request, response); // JWT 쿠키가 없으면 통과
            return;
        }

        // 2. 블랙리스트에 있는지 확인
        if (userService.isTokenBlacklisted(jwtCookie)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("블랙리스트에 등록된 토큰입니다.");
            return; // 더 이상 진행하지 않고 종료
        }

        // 3. 유효기간, 위조여부 확인해보고
        Claims claim;
        try { // 에러가 날 수 있으므로 try-catch
            claim = JwtUtil.extractToken(jwtCookie);
        } catch (Exception e) {
            filterChain.doFilter(request, response); // 다음 필터 실행
            return;
        }

        System.out.println("Claims: " + claim);

        // 4. 문제없으면 auth 변수에 유저정보 입력
        var arr = claim.get("authorities").toString().split(","); // 권한들을 리스트에 담음
        var authorities = Arrays.stream(arr).map(a -> new SimpleGrantedAuthority(a)).toList(); // 권한 설정

        var customUser = new CustomUser(
            claim.get("cellphone").toString(),
            "none",
            authorities
        );
        customUser.setId(((Number) claim.get("id")).longValue()); // id 설정
        customUser.setName(claim.get("name").toString()); // name 설정

        var authToken = new UsernamePasswordAuthenticationToken(
            customUser, null, authorities
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken); // 유저정보 추가

        filterChain.doFilter(request, response); // 다음 필터 실행
    }
}


