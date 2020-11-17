package ru.catstack.nfc_terminal.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.catstack.nfc_terminal.model.UserStatus;

import java.util.Collection;
import java.util.List;

public class JwtUser implements UserDetails {
    private final long id;
    private final String username;
    private final String password;
    private final UserStatus userStatus;
    private final GrantedAuthority authorities;

    JwtUser(long id, String username, String password, GrantedAuthority authority, UserStatus userStatus) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authority;
        this.userStatus = userStatus;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !this.userStatus.equals(UserStatus.LOCKED);
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.authorities);
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return userStatus.equals(UserStatus.ACTIVE);
    }

    @JsonIgnore
    public long getId() {
        return id;
    }
}
