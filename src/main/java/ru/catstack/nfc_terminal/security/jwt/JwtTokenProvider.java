package ru.catstack.nfc_terminal.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.catstack.nfc_terminal.exception.InvalidJwtTokenException;
import ru.catstack.nfc_terminal.security.JwtUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt.secret}")
    private static String secret;

    @Value("${app.jwt.prefix}")
    private static String tokenPrefix;

    @Value("${app.jwt.header}")
    private static String tokenHeader;

    private final JwtUserDetailsService userDetailsService;

    public JwtTokenProvider(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @PostConstruct
//    protected void init() {
//        secret = Base64.getEncoder().encodeToString(secret.getBytes());
//    }

    public String createToken(@NotNull JwtUser user) {
        return Jwts.builder()
                .setId(Long.toString(user.getId()))
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    Authentication getAuthentication(String token) {
        var userDetails = userDetailsService.loadById(getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null);
    }

    public Long getUserId(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getId());
    }

    public String resolveToken(@NotNull HttpServletRequest req) {
        var bearerToken = req.getHeader(tokenHeader);
        if (bearerToken == null || !bearerToken.startsWith(tokenPrefix + " "))
            throw new InvalidJwtTokenException("JWT token is invalid");
        return bearerToken.substring(7);
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }
}
