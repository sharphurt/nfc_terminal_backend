package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.model.DeviceInfo;
import ru.catstack.nfc_terminal.model.Session;
import ru.catstack.nfc_terminal.model.payload.request.LoginRequest;
import ru.catstack.nfc_terminal.repository.SessionRepository;
import ru.catstack.nfc_terminal.security.jwt.JwtUser;

import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    Optional<Session> findByUserIdAndDeviceId(long userId, String deviceId) {
        return sessionRepository.findByUserIdAndDeviceId(userId, deviceId);
    }

    public Optional<Session> findBySessionId(long id) {
        return sessionRepository.findById(id);
    }

    void deleteBySessionId(long sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    void deleteByDeviceIdAndUserId(String deviceId, Long userId) {
        sessionRepository.deleteByDeviceIdAndUserId(deviceId, userId);
    }

    public Session createSession(Authentication auth, LoginRequest loginRequest) {
        var user = (JwtUser) auth.getPrincipal();
        if (isDeviceAlreadyExists(loginRequest.getDeviceInfo())) {
            var session = findByUserIdAndDeviceId(user.getId(), loginRequest.getDeviceInfo().getDeviceId());
            session.ifPresent(sess -> deleteBySessionId(sess.getId()));
        }
        var newSession = new Session(user.getId(), loginRequest.getDeviceInfo());
        return save(newSession);
    }

    Session save(Session session) {
        return sessionRepository.save(session);
    }

    boolean isDeviceAlreadyExists(DeviceInfo deviceInfo) {
        return sessionRepository.existsByDeviceId(deviceInfo.getDeviceId());
    }
}
