package com.capstone03.goldenglobe.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final BCryptPasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  // 회원 가입
  @PostMapping("/auth/signup")
  public ResponseEntity<String> registerUser(@RequestBody User user) {
    if (user.getCellphone() == null || user.getPassword() == null) {
      return new ResponseEntity<>("전화번호와 비밀번호는 필수입니다.", HttpStatus.BAD_REQUEST);
    }
    if (userService.findByCellphone(user.getCellphone()).isPresent()) {
      return new ResponseEntity<>("이미 가입된 전화번호입니다.", HttpStatus.CONFLICT);
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userService.saveUser(user);

    return new ResponseEntity<>("성공적으로 회원가입되었습니다.", HttpStatus.CREATED);
  }

  // 로그인
  @PostMapping("/auth/signin")
  public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
    String cellphone = loginRequest.get("cellphone");
    String password = loginRequest.get("password");

    try {
      // 1. 로그인 인증
      var authToken = new UsernamePasswordAuthenticationToken(cellphone, password);
      var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
      SecurityContextHolder.getContext().setAuthentication(auth);

      // 2. 액세스 토큰 및 리프레시 토큰 생성
      var jwt = JwtUtil.createToken(auth);
      var refreshToken = JwtUtil.createRefreshToken(auth);

      // 3. 리프레시 토큰을 DB에 저장 (유저별로 관리)
      userService.updateRefreshToken(cellphone, refreshToken);

      // 4. 쿠키에 액세스 토큰 저장
      var jwtCookie = new Cookie("jwt", jwt);
      jwtCookie.setMaxAge(600); // 유효기간 600초 설정 (예: 10분)
      jwtCookie.setHttpOnly(true);
      jwtCookie.setPath("/");

      // 5. 쿠키에 리프레시 토큰 저장
      var refreshTokenCookie = new Cookie("refreshToken", refreshToken);
      refreshTokenCookie.setMaxAge(86400); // 유효기간 1일 설정
      refreshTokenCookie.setHttpOnly(true);
      refreshTokenCookie.setPath("/");

      response.addCookie(jwtCookie);
      response.addCookie(refreshTokenCookie);

      // 6. 응답에 토큰 정보 포함
      Map<String, String> tokens = new HashMap<>();
      tokens.put("accessToken", jwt);
      tokens.put("refreshToken", refreshToken);

      return new ResponseEntity<>(tokens, HttpStatus.OK);

    } catch (BadCredentialsException e) {
      return new ResponseEntity<>(Map.of("error", "잘못된 접근"), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      return new ResponseEntity<>(Map.of("error", "로그인 실패"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // 로그아웃
  @PostMapping("/auth/logout")
  public ResponseEntity<String> logoutUser(HttpServletRequest request) {
    // 쿠키에서 JWT 토큰 가져오기
    Cookie[] cookies = request.getCookies();
    String jwtToken = null;

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("jwt".equals(cookie.getName())) {
          jwtToken = cookie.getValue();
          break;
        }
      }
    }

    if (jwtToken != null) {
      // JWT 토큰을 블랙리스트에 추가
      userService.blacklistToken(jwtToken);

      // JWT와 리프레시 토큰을 삭제하기 위해 쿠키를 만료 처리
      Cookie jwtCookie = new Cookie("jwt", null);
      jwtCookie.setMaxAge(0);
      jwtCookie.setPath("/");
      HttpServletResponse response = (HttpServletResponse) request;
      response.addCookie(jwtCookie);

      Cookie refreshTokenCookie = new Cookie("refreshToken", null);
      refreshTokenCookie.setMaxAge(0);
      refreshTokenCookie.setPath("/");
      response.addCookie(refreshTokenCookie);

      return new ResponseEntity<>("로그아웃 성공!", HttpStatus.OK);
    }

    return new ResponseEntity<>("토큰을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
  }

  // 사용자 정보 조회
  @GetMapping("/myPage/{user_id}")
  public ResponseEntity<User> getUserInfo(@PathVariable("user_id") Long userId) {
    Optional<User> userOptional = userService.findById(userId);
    if (userOptional.isPresent()) {
      return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  // 사용자 정보 수정
  @PutMapping("/myPage/modifyProfile/{user_id}")
  public ResponseEntity<String> updateUserInfo(@PathVariable("user_id") Long userId, @RequestBody User updatedUser) {
    Optional<User> userOptional = userService.findById(userId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      // 필요한 필드 업데이트 (비밀번호 암호화도 가능)
      user.setName(updatedUser.getName());
      user.setNickname(updatedUser.getNickname());
      user.setCellphone(updatedUser.getCellphone());
      user.setProfile(updatedUser.getProfile());
      user.setGender(updatedUser.getGender());
      userService.saveUser(user);
      return new ResponseEntity<>("사용자 정보가 성공적으로 수정되었습니다.", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("사용자가 존재하지 않습니다", HttpStatus.NOT_FOUND);
    }
  }

  // 회원 탈퇴
  @DeleteMapping("/users/{user_id}")
  public ResponseEntity<String> deleteUser(@PathVariable("user_id") Long userId) {
    Optional<User> userOptional = userService.findById(userId);
    if (userOptional.isPresent()) {
      userService.deleteUser(userId);
      return new ResponseEntity<>("성공적으로 탈퇴처리되었습니다.", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("사용자가 존재하지 않습니다", HttpStatus.NOT_FOUND);
    }
  }
}
