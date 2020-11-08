package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.model.DeviceInfo;
import ru.catstack.nfc_terminal.model.Session;
import ru.catstack.nfc_terminal.repository.SessionRepository;

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

    void deleteBySessionId(long sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    void deleteByDeviceIdAndUserId(String deviceId, Long userId) {
        sessionRepository.deleteByDeviceIdAndUserId(deviceId, userId);
    }

    void save(Session session) {
        sessionRepository.save(session);
    }

    boolean isDeviceAlreadyExists(DeviceInfo deviceInfo) {
        return sessionRepository.existsByDeviceId(deviceInfo.getDeviceId());
    }
}
