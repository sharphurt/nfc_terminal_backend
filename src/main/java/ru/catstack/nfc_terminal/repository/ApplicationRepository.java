package ru.catstack.nfc_terminal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.nfc_terminal.model.Application;
import ru.catstack.nfc_terminal.model.enums.ApplicationStatus;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findAllByStatus(Pageable pageable, ApplicationStatus status);


    @Transactional
    @Modifying
    @Query("UPDATE Application a SET a.status = :status WHERE a.id = :id")
    void updateStatusById(@Param("id") long id, @Param("status") ApplicationStatus status);


    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByInn(long name);

}
