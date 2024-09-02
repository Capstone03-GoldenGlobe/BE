package com.capstone03.goldenglobe.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@ToString
public class CustomUser extends User implements UserDetails {
    private Long id;
    private String name;
    private String cellphone;

    public CustomUser(
        String cellphone,
        String password,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(cellphone, password, authorities);
        this.cellphone = cellphone;
    }

    @Override
    public String getUsername() {
        return cellphone;
    }
}
