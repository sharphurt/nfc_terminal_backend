package ru.catstack.nfc_terminal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.catstack.nfc_terminal.exception.handler.CustomAccessDeniedHandler;
import ru.catstack.nfc_terminal.security.jwt.JwtTokenFilter;
import ru.catstack.nfc_terminal.security.jwt.JwtTokenProvider;

import java.util.List;


@EnableConfigurationProperties
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final List<String> publicEndpoints = List.of(
            "/api/auth/login",
            "/api/email/send",
            "/api/auth/checkPhone",
            "/api/applications/send",
            "/api/check/email",
            "/api/check/username",
            "/api/check/inn",
            "/api/auth/admin/register"
    );

    public static final List<String> authenticatedEndpoints = List.of(
            "/api/auth/logout",
            "/api/users/me",
            "/api/companies/create",
            "/api/companies\\/\\d+",
            "/api/payment/create",
            "/api/history/get",
            "/api/auth/register",
            "/api/applications/get",
            "/api/applications/reject"
    );

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(publicEndpoints.toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtTokenFilter(tokenProvider, authenticatedEndpoints), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint());
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAccessDeniedHandler();
    }
}
