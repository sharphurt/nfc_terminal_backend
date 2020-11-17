package ru.catstack.nfc_terminal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.nfc_terminal.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
