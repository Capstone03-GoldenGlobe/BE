package com.capstone03.goldenglobe.user;

import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //입력한 email로 db에서 user 찾기
        var result =  userRepository.findByEmail(email);
        if(result.isEmpty()){
            throw new UsernameNotFoundException("아이디를 찾을 수 없습니다.");
        }
        var user = result.get();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("일반유저"));

        var customUser = new CustomUser(user.getEmail(),user.getPassword(), authorities); //아이디,비번,권한
        customUser.setId(user.getUserId());
        customUser.setName(user.getName());
        customUser.setEmail(user.getEmail());
        System.out.println(customUser.getId());
        return customUser;
    }
}
