package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.AccessDeniedException;
import ru.catstack.nfc_terminal.exception.ResourceAlreadyInUseException;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.User;
import ru.catstack.nfc_terminal.model.payload.request.RegistrationRequest;
import ru.catstack.nfc_terminal.repository.UserRepository;
import ru.catstack.nfc_terminal.security.jwt.JwtTokenProvider;
import ru.catstack.nfc_terminal.security.jwt.JwtUser;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByIdOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    User save(User user) {
        return userRepository.save(user);
    }

    boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    User createUser(RegistrationRequest request) {
        var user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getUsername(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getPhone().replace("+", ""));
        return save(user);
    }

    public User getLoggedInUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken)
            throw new AccessDeniedException("Unexpected error");
        return findByIdOrThrow(((JwtUser) auth.getPrincipal()).getId());
    }

    public void updateUsernameById(Long id, String username) {
        if (existsByUsername(username))
            throw new ResourceAlreadyInUseException("Username", "value", username);
        userRepository.updateUsernameById(id, username);
        setUpdatedAtById(id, Instant.now());
    }

    public void updateFirstNameById(Long id, String firstName) {
        userRepository.updateFirstNameById(id, firstName);
        setUpdatedAtById(id, Instant.now());
    }

    public void updateLastNameById(Long id, String lastName) {
        userRepository.updateLastNameById(id, lastName);
        setUpdatedAtById(id, Instant.now());
    }

    public void updateEmailById(Long id, String email) {
        if (existsByEmail(email))
            throw new ResourceAlreadyInUseException("Email", "value", email);
        userRepository.updateEmailById(id, email);
        setUpdatedAtById(id, Instant.now());
    }

    public void updatePasswordById(Long id, String password) {
        userRepository.updatePasswordById(id, passwordEncoder.encode(password));
        setUpdatedAtById(id, Instant.now());
    }

    public void increaseLoginsCountById(long id) {
        userRepository.findById(id).ifPresent(u ->
            userRepository.updateLoginsCountById(u.getId(), u.getLoginsCount() + 1)
        );
    }

    private void setUpdatedAtById(Long id, Instant updatedAt) {
        userRepository.setUpdatedAtById(id, updatedAt);
    }
}
