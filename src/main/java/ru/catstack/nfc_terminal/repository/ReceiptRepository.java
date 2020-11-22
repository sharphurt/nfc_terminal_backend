package ru.catstack.nfc_terminal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.nfc_terminal.model.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
