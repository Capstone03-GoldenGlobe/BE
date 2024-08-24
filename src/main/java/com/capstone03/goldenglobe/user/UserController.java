package com.capstone03.goldenglobe.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

  private final UserService userService;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  // 회원 가입
  @PostMapping("/auth/signup")
  public ResponseEntity<String> registerUser(@RequestBody User user) {
    // 비밀번호 암호화
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userService.saveUser(user);
    return new ResponseEntity<>("성공적으로 회원가입되었습니다.", HttpStatus.CREATED);
  }

  // 로그인
  @PostMapping("/auth/signin")
  public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
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
  @PostMapping("/myPage/{user_id}")
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
}
