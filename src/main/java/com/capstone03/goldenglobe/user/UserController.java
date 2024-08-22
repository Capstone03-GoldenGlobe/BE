package com.capstone03.goldenglobe.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

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
    return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
  }

  // 로그인
  @PostMapping("/auth/signin")
  public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
    Optional<User> userOptional = userService.findByEmail(email);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (passwordEncoder.matches(password, user.getPassword())) {
        return new ResponseEntity<>("Login successful", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
      }
    } else {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
  }

  // 사용자 정보 수정
  @PutMapping("/users/{user_id}")
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
      return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
  }

  // 아이디 찾기
  @PostMapping("/users/help/idInquiry")
  public ResponseEntity<String> findUserId(@RequestParam String email) {
    Optional<User> userOptional = userService.findByEmail(email);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return new ResponseEntity<>("Your user ID is: " + user.getUserId(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
  }

  // 비밀번호 찾기
  @PostMapping("/users/help/pwInquiry")
  public ResponseEntity<String> findUserPassword(@RequestParam String email) {
    Optional<User> userOptional = userService.findByEmail(email);
    if (userOptional.isPresent()) {
      // 비밀번호 찾기 로직 (예: 임시 비밀번호 발급 후 이메일 전송)
      // 이 부분은 실제 구현 시 구체화해야 합니다.
      return new ResponseEntity<>("Password reset instructions sent to your email", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
  }

  // 회원 탈퇴
  @DeleteMapping("/users/{user_id}")
  public ResponseEntity<String> deleteUser(@PathVariable("user_id") Long userId) {
    Optional<User> userOptional = userService.findById(userId);
    if (userOptional.isPresent()) {
      userService.deleteUser(userId);
      return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
  }
}
