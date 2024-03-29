package ru.catstack.nfc_terminal.repository;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.catstack.nfc_terminal.model.User;
import ru.catstack.nfc_terminal.model.enums.UserStatus;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.firstName = :firstName WHERE c.id = :id")
    void updateFirstNameById(@Param("id") Long id, @Param("firstName") String firstName);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.lastName = :lastName WHERE c.id = :id")
    void updateLastNameById(@Param("id") Long id, @Param("lastName") String lastName);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.email = :email WHERE c.id = :id")
    void updateEmailById(@Param("id") Long id, @Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.password = :password WHERE c.id = :id")
    void updatePasswordById(@Param("id") Long id, @Param("password") String password);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.updatedAt = :updatedAt WHERE c.id = :id")
    void setUpdatedAtById(@Param("id") Long id, @Param("updatedAt") Instant updatedAt);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.loginsCount = :newLG WHERE c.id = :id")
    void updateLoginsCountById(@Param("id") Long id, @Param("newLG") long newLG);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.userStatus = :status WHERE c.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") UserStatus status);

    Page<User> findAll(@NotNull Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}