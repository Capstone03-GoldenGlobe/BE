package com.capstone03.goldenglobe.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String cellphone) throws UsernameNotFoundException {
        // 입력한 전화번호로 DB에서 사용자 찾기
        Optional<User> result = userRepository.findByCellphone(cellphone);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("아이디를 찾을 수 없습니다.");
        }
        User user = result.get();

        // 권한을 user.getRoles()에서 동적으로 가져오기
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // "ROLE_" prefix를 추가
            .collect(Collectors.toList());

        // CustomUser 객체 생성
        CustomUser customUser = new CustomUser(user.getCellphone(), user.getPassword(), authorities);
        customUser.setId(user.getUserId());
        customUser.setName(user.getName());

        return customUser;
    }
}
