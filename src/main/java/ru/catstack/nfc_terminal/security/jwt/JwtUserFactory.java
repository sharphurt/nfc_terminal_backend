package ru.catstack.nfc_terminal.security.jwt;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.catstack.nfc_terminal.model.User;

public final class JwtUserFactory {
    public static JwtUser fromUser(User user) {
        return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), new SimpleGrantedAuthority("USER"), user.getUserStatus());
    }
}
