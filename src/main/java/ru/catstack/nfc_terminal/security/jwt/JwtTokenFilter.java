package ru.catstack.nfc_terminal.security.jwt;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.catstack.nfc_terminal.exception.InvalidJwtTokenException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final List<String> authenticatedRequestPatterns;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, List<String> authenticatedRequestPatterns) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticatedRequestPatterns = authenticatedRequestPatterns;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (NeedToCheckRequest(request)) {
            try {
                var token = jwtTokenProvider.resolveToken(request);
                if (jwtTokenProvider.isTokenValid(token)) {
                    var authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else
                    throw new InvalidJwtTokenException("Request requires authentication");
            } catch (Exception ex) {
                throw new InvalidJwtTokenException("Request requires authentication");
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean NeedToCheckRequest(HttpServletRequest request) {
        for (var pattern : this.authenticatedRequestPatterns)
            if (request.getRequestURI().matches(pattern))
                return true;
        return false;
    }

}
