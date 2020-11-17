package ru.catstack.nfc_terminal.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.catstack.nfc_terminal.exception.InvalidJwtTokenException;
import ru.catstack.nfc_terminal.model.Session;
import ru.catstack.nfc_terminal.security.JwtUserDetailsService;
import ru.catstack.nfc_terminal.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@EnableConfigurationProperties
@Component
public class JwtTokenProvider {
    private static final String secret = "mySecret";
    private static final String tokenHeader = "Authorization";
    private static final String tokenPrefix = "Bearer";

    private final JwtUserDetailsService userDetailsService;
    private final SessionService sessionService;

    @Autowired
    public JwtTokenProvider(JwtUserDetailsService userDetailsService, SessionService sessionService) {
        this.userDetailsService = userDetailsService;
        this.sessionService = sessionService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String createToken(@NotNull JwtUser user, @NotNull Session session) {
        return Jwts.builder()
                .setId(String.valueOf(session.getUniqueKey()))
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    Authentication getAuthentication(String token) {
        var userDetails = userDetailsService.loadById(getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public long getUniqueKey(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getId());
    }

    public long getUserId(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject());
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

    public boolean isTokenValid(String token) {
        return token != null && !token.equals("") && sessionService.existsByUniqueKey(getUniqueKey(token));
    }
}
