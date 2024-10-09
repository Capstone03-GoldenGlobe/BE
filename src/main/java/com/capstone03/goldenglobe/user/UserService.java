package com.capstone03.goldenglobe.user;

import com.capstone03.goldenglobe.user.refreshToken.RefreshToken;
import com.capstone03.goldenglobe.user.refreshToken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  // 현재 로그인한 사용자 이름 가져오기
  public String getLoggedInUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      return userDetails.getUsername();
    }
    return "로그인하세요";
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
      user.setPassword(temporaryPassword); // 일단 임시 - 실제로 암호화되어야 함
      saveUser(user);
      // SMS 전송 로직 추가 가능
      return "초기화된 비밀번호가 휴대폰으로 전송되었습니다.";
    }
    return "사용자가 존재하지 않습니다.";
  }

  // 임시 비밀번호 생성
  private String generateTemporaryPassword() {
    // 일단 임시 - 비밀번호 생성하는 로직 구현하기
    return "temppw123";
  }

  // 리프레시 토큰 업데이트
  public void updateRefreshToken(String cellphone, String refreshToken) {
    Optional<User> userOptional = userRepository.findByCellphone(cellphone);
    if (userOptional.isPresent()) {
      User user = userOptional.get();

      RefreshToken refreshToKen = new RefreshToken(user.getUserId(), refreshToken);
      refreshTokenRepository.save(refreshToKen);
    }
  }
}

