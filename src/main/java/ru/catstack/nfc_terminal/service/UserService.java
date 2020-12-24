package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.AccessDeniedException;
import ru.catstack.nfc_terminal.exception.ResourceAlreadyInUseException;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.User;
import ru.catstack.nfc_terminal.model.enums.UserPrivilege;
import ru.catstack.nfc_terminal.model.enums.UserStatus;
import ru.catstack.nfc_terminal.model.payload.request.AdminRegistrationRequest;
import ru.catstack.nfc_terminal.model.payload.request.ClientRequestBody;
import ru.catstack.nfc_terminal.repository.UserRepository;
import ru.catstack.nfc_terminal.security.jwt.JwtTokenProvider;
import ru.catstack.nfc_terminal.security.jwt.JwtUser;
import ru.catstack.nfc_terminal.util.OffsetBasedPage;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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

    boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    User createAdmin(AdminRegistrationRequest request) {
        var user = new User(request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                request.getPhone(),
                UserPrivilege.ADMIN);
        return save(user);
    }

    User createClient(ClientRequestBody request) {
        var user = new User(request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName(),
                request.getPatronymic(),
                request.getPhone(),
                UserPrivilege.CLIENT);
        return save(user);
    }

    public User getLoggedInUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken)
            throw new AccessDeniedException("Unexpected error");
        return findByIdOrThrow(((JwtUser) auth.getPrincipal()).getId());
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

    public void updateStatusById(Long id, UserStatus status) {
        userRepository.updateStatusById(id, status);
        setUpdatedAtById(id, Instant.now());
    }

    public void increaseLoginsCountById(long id) {
        userRepository.findById(id).ifPresent(u ->
                userRepository.updateLoginsCountById(u.getId(), u.getLoginsCount() + 1)
        );
    }

    public List<User> getUsersGap(int from, int count) {
        if (getLoggedInUser().getUserPrivilege() != UserPrivilege.ADMIN)
            throw new AccessDeniedException("You don't have permission to make this request");

        return findAllByStatus(new OffsetBasedPage(from, count, sort)).getContent();
    }

    public Page<User> findAllByStatus(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void setStatusById(long id, UserStatus status) {
        if (getLoggedInUser().getUserPrivilege() != UserPrivilege.ADMIN)
            throw new AccessDeniedException("You don't have permission to make this request");
        userRepository.updateStatusById(id, status);
    }

    private void setUpdatedAtById(Long id, Instant updatedAt) {
        userRepository.setUpdatedAtById(id, updatedAt);
    }
}
