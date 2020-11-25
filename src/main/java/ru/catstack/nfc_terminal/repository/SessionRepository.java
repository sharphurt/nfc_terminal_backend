package ru.catstack.nfc_terminal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.nfc_terminal.model.Session;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByUserIdAndDeviceId(Long userId, String deviceId);

    @Transactional
    void deleteByDeviceIdAndUserId(String deviceId, Long userId);

    @Transactional
    void deleteById(long id);

    boolean existsByDeviceId(String deviceId);

    boolean existsByUniqueKey(long key);
}
