package ru.catstack.nfc_terminal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.nfc_terminal.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
