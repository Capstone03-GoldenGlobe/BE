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

  // 전화번호로 사용자 찾기
  public Optional<User> findByCellphone(String cellphone) {
    return userRepository.findByCellphone(cellphone);
  }

  // 사용자 ID로 사용자 찾기
  public Optional<User> findById(Long userId) {
    return userRepository.findById(userId);
  }

  // 사용자 삭제
  public void deleteUser(Long userId) {
    userRepository.deleteById(userId);
  }

  // 비밀번호 재설정 로직
  public String resetPassword(String cellphone) {
    Optional<User> userOptional = findByCellphone(cellphone);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      String temporaryPassword = generateTemporaryPassword();
      user.setPassword(temporaryPassword); // 일단임시 - 실제로 암호화되어야 함
      saveUser(user);
      // SMS 전송 로직 추가 가능
      return "Password reset instructions sent to your phone";
    }
    return "User not found";
  }

  // 임시 비밀번호 생성
  private String generateTemporaryPassword() {
    // 일단임시 - 비밀번호 생성하는 로직 구현하기
    return "temporaryPassword123";
  }

  // 리프레시 토큰 업데이트
  public void updateRefreshToken(String cellphone, String refreshToken) {
    Optional<User> userOptional = userRepository.findByCellphone(cellphone);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setRefreshToken(refreshToken); // 리프레시 토큰 설정
      userRepository.save(user); // 업데이트된 사용자 정보 저장
    }
  }
}
