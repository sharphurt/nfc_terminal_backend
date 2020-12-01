package ru.catstack.nfc_terminal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.nfc_terminal.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findAllBy(Pageable pageable);


    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByInn(long name);

}
