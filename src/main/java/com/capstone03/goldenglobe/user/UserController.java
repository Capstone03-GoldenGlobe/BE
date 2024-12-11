package com.capstone03.goldenglobe.user;

import com.capstone03.goldenglobe.user.blackList.BlackListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final BCryptPasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final BlackListService blackListService;
  private final JwtUtil jwtUtil;

  // 회원 가입
  @Transactional
  @PostMapping("/auth/signup")
  public ResponseEntity<String> registerUser(@RequestBody User user) {
    if (user.getCellphone() == null || user.getPassword() == null) {
      return new ResponseEntity<>("전화번호와 비밀번호는 필수입니다.", HttpStatus.BAD_REQUEST);
    }
    if (userService.findByCellphone(user.getCellphone()).isPresent()) {
      return new ResponseEntity<>("이미 가입된 전화번호입니다.", HttpStatus.CONFLICT);
    }
    if (userService.isNicknameTaken(user.getNickname())) {
      return new ResponseEntity<>("이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT);
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // 기본 권한 추가
    Set<String> roles = new HashSet<>();
    roles.add("USER"); // USER, ADMIN
    user.setRoles(roles);

    // 사용자 저장
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

      // 3. 리프레시 토큰을 DB에 저장
      userService.updateRefreshToken(cellphone, refreshToken);

      // 4. 응답에 리프레시 토큰 정보 포함
      Map<String, String> tokens = new HashMap<>();
      tokens.put("refreshToken", refreshToken);

      // 5. 헤더에 액세스 토큰 추가
      response.addHeader("Authorization", "Bearer " + jwt);
      tokens.put("Authorization","Bearer "+jwt);
      System.out.println(jwt);

      return new ResponseEntity<>(tokens, HttpStatus.OK);
    } catch (BadCredentialsException e) {
      return new ResponseEntity<>(Map.of("error", "잘못된 접근"), HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      return new ResponseEntity<>(Map.of("error", "로그인 실패"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // 로그아웃
  @PostMapping("/auth/logout")
  public ResponseEntity<Map<String, String>> logoutUser(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      return new ResponseEntity<>(Map.of("error", "토큰을 찾을 수 없습니다."), HttpStatus.BAD_REQUEST);
    }

    // 액세스 토큰 추출
    String accessToken = authorizationHeader.substring(7);

    // 액세스 토큰의 만료 시간 가져오기
    Date expirationDate = jwtUtil.getExpiration(accessToken);

    // 블랙리스트에 추가
    blackListService.addToBlacklist(accessToken, expirationDate);

    return new ResponseEntity<>(Map.of("message", "로그아웃 성공"), HttpStatus.OK);
  }

  // 사용자 정보 조회
  @GetMapping("/myPage")
  public ResponseEntity<UserDTO> getUserInfo(Authentication auth) {
    CustomUser customUser = (CustomUser) auth.getPrincipal();
    Long authUserId = customUser.getId();
    Optional<User> userOptional = userService.findById(authUserId);
    if (userOptional.isPresent()) {
      UserDTO userDTO = UserDTO.fromEntity(userOptional.get());
      return new ResponseEntity<>(userDTO, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  // 사용자 정보 수정
  @PutMapping("/myPage/modifyProfile")
  public ResponseEntity<String> updateUserInfo(@RequestBody User updatedUser, Authentication auth) {
    CustomUser customUser = (CustomUser) auth.getPrincipal();
    Long authUserId = customUser.getId();
    Optional<User> userOptional = userService.findById(authUserId);

    if (userOptional.isPresent()) {
      User user = userOptional.get();

      // 닉네임 중복 확인 로직 추가
      if (!user.getNickname().equals(updatedUser.getNickname()) &&
          userService.isNicknameTaken(updatedUser.getNickname())) {
        return new ResponseEntity<>("이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT);
      }

      // 필요한 필드 업데이트
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
  @DeleteMapping("/users")
  public ResponseEntity<String> deleteUser(Authentication auth) {
    CustomUser customUser = (CustomUser) auth.getPrincipal();
    Long authUserId = customUser.getId();
    Optional<User> userOptional = userService.findById(authUserId);

    if (userOptional.isPresent()) {
      userService.deleteUser(authUserId);
      return new ResponseEntity<>("성공적으로 탈퇴처리되었습니다.", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("사용자가 존재하지 않습니다", HttpStatus.NOT_FOUND);
    }
  }

  // 리프레시 토큰으로 액세스 토큰 재발급
}
