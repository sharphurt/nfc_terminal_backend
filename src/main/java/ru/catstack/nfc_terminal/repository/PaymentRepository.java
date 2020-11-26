package ru.catstack.nfc_terminal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.nfc_terminal.model.Payment;
import ru.catstack.nfc_terminal.model.enums.PaymentStatus;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByIdempotenceKey(long key);

    Optional<Payment> findByIdempotenceKey(long key);

    @Transactional
    @Modifying
    @Query("UPDATE Payment p SET p.status = :status WHERE p.idempotenceKey = :key")
    void updateStatusByIdempotenceKey(@Param("key") long key, @Param("status") PaymentStatus status);

}
