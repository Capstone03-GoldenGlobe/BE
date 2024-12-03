package com.capstone03.goldenglobe.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  // 전화번호로 사용자 찾기
  Optional<User> findByCellphone(String cellphone);

  Optional<User> findByNickname(String nickname);

}
