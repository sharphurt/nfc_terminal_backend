package ru.catstack.nfc_terminal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.nfc_terminal.model.Company;
import ru.catstack.nfc_terminal.model.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Page<Receipt> findAllByCompany(Company company, Pageable pageable);
}
