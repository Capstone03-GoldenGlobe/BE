package com.capstone03.goldenglobe.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    if (user.getEmail() == null || user.getPassword() == null) {
      return new ResponseEntity<>("이메일과 비밀번호는 필수입니다.", HttpStatus.BAD_REQUEST);
    }
    if (userService.findByEmail(user.getEmail()).isPresent()) {
      return new ResponseEntity<>("이미 가입된 이메일입니다.", HttpStatus.CONFLICT);
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userService.saveUser(user);

    return new ResponseEntity<>("성공적으로 회원가입되었습니다.", HttpStatus.CREATED);
  }


  // 로그인
  @PostMapping("/auth/signin")
  public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginRequest) {
    String email = loginRequest.get("email");
    String password = loginRequest.get("password");

    Optional<User> userOptional = userService.findByEmail(email);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (passwordEncoder.matches(password, user.getPassword())) {
        return new ResponseEntity<>("로그인 성공!", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("잘못된 접근", HttpStatus.UNAUTHORIZED);
      }
    } else {
      return new ResponseEntity<>("사용자가 존재하지 않습니다", HttpStatus.NOT_FOUND);
    }
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


  // 아이디 찾기
  @PostMapping("/users/help/idInquiry")
  public ResponseEntity<String> findUserId(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    Optional<User> userOptional = userService.findByEmail(email);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return new ResponseEntity<>("ID: " + user.getUserId(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>("사용자가 존재하지 않습니다", HttpStatus.NOT_FOUND);
    }
  }


  // 비밀번호 찾기
  @PostMapping("/users/help/pwInquiry")
  public ResponseEntity<String> findUserPassword(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    Optional<User> userOptional = userService.findByEmail(email);

    if (userOptional.isPresent()) {
      // 일단 임시 - 나중에 구현하기
      return new ResponseEntity<>("암호 재설정 지침이 이메일로 전송되었습니다.", HttpStatus.OK);
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

  @PostMapping("/login/jwt")
  @ResponseBody
  public String loginJWT(@RequestBody Map<String,String> data, HttpServletResponse response){
    //1. 로그인
    var authToken = new UsernamePasswordAuthenticationToken(data.get("email"),data.get("password"));
    var auth = authenticationManagerBuilder.getObject().authenticate(authToken); // 아이디/비번을 DB와 비교해서 로그인

    SecurityContextHolder.getContext().setAuthentication(auth);

    //2. 입장권 배부
    var jwt = JwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());
    System.out.println(jwt);

    //3. 쿠키에 jwt 저장
    var cookie = new Cookie("jwt",jwt);
    cookie.setMaxAge(100); //유효기간 100초로 설정
    cookie.setHttpOnly(true);
    cookie.setPath("/"); //쿠키가 전송될 URL : 모든 경로
    response.addCookie(cookie); //유저 브라우저에 강제로 쿠키 저장

    return jwt;
  }

  @GetMapping("/jwttest")
  @ResponseBody
  String mypageJWT(Authentication auth){
    var user = (CustomUser) auth.getPrincipal();
    System.out.println(user);
    System.out.println(user.getEmail());
    System.out.println(user.getAuthorities());
    String userInfo = "Email: " + user.getEmail()+ ", Authorities: " + user.getAuthorities()+"\n"+user;
    return userInfo;
  }
}
