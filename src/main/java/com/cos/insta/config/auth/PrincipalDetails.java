package com.cos.insta.config.auth;

import com.cos.insta.domain.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private static final long serialVersionUID = 1L; // 객체를 구분할 때

    private User user;
    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
    }


    @Override//
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collector = new ArrayList<>();
        collector.add(() -> user.getRole());
        return collector;
    }

    @Override//
    public String getPassword() {
        return user.getPassword();
    }

    @Override//
    public String getUsername() {
        return user.getUsername();
    }

    @Override//
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override//
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override//
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override//
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes; // ex {id:156165, name:신동아, email:sssssr@facebook}
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
