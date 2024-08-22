package com.capstone03.goldenglobe.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // 사용자 저장
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  // 이메일로 사용자 찾기
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  // 사용자 ID로 사용자 찾기
  public Optional<User> findById(Long userId) {
    return userRepository.findById(userId);
  }

  // 사용자 삭제
  public void deleteUser(Long userId) {
    userRepository.deleteById(userId);
  }

  // 비밀번호 재설정 로직 (예시)
  public String resetPassword(String email) {
    Optional<User> userOptional = findByEmail(email);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      String temporaryPassword = generateTemporaryPassword();
      user.setPassword(temporaryPassword); // 이 비밀번호는 실제로 암호화되어야 함
      saveUser(user);
      // 이메일 전송 로직 추가 가능
      return "Password reset instructions sent to your email";
    }
    return "User not found";
  }

  // 임시 비밀번호 생성 (예시)
  private String generateTemporaryPassword() {
    // 여기서 임시 비밀번호를 생성하는 로직을 구현합니다.
    return "temporaryPassword123";
  }
}
