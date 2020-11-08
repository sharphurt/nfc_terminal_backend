package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.AccessDeniedException;
import ru.catstack.nfc_terminal.exception.ResourceAlreadyInUseException;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.Role;
import ru.catstack.nfc_terminal.model.User;
import ru.catstack.nfc_terminal.model.payload.request.RegistrationRequest;
import ru.catstack.nfc_terminal.repository.UserRepository;
import ru.catstack.nfc_terminal.security.jwt.JwtTokenProvider;
import ru.catstack.nfc_terminal.security.jwt.JwtUser;

import java.time.Instant;

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

    public User findByEmailOrThrow(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    public User findByIdOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    User save(User user) {
        return userRepository.save(user);
    }

    Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    User createUser(RegistrationRequest request) {
        return new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getUsername(),
                request.getFirstName(), request.getLastName(), Role.valueOf(request.getRole()));
    }

    public User getLoggedInUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken)
            throw new AccessDeniedException("Unexpected error");
        return findByIdOrThrow(((JwtUser) auth.getPrincipal()).getId());
    }

    public void updateAvatarById(Long id, String avatarCode) {
        userRepository.updateAvatarById(id, avatarCode);
        setUpdatedAtById(id, Instant.now());
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

    private void setUpdatedAtById(Long id, Instant updatedAt) {
        userRepository.setUpdatedAtById(id, updatedAt);
    }
}
