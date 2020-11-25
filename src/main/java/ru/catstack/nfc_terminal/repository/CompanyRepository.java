package ru.catstack.nfc_terminal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.catstack.nfc_terminal.model.Company;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByInn(long inn);

    boolean existsByInn(long inn);

    boolean existsByKkt(long kkt);

    boolean existsByCompanyName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE Bill b SET b.balance = :balance WHERE b.id = :id")
    void updateBalanceById(@Param("id") long id, @Param("balance") float balance );

}
