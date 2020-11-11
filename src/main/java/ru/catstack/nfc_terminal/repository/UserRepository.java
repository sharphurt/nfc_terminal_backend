package ru.catstack.nfc_terminal.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.catstack.nfc_terminal.model.User;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User c SET c.username = :username WHERE c.id = :id")
    void updateUsernameById(@Param("id") Long id, @Param("username") String username);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.firstName = :firstName WHERE c.id = :id")
    void updateFirstNameById(@Param("id") Long id, @Param("firstName") String firstName);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.lastName = :lastName WHERE c.id = :id")
    void updateLastNameById(@Param("id") Long id, @Param("lastName") String lastName);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.email = :email WHERE c.id = :id")
    void updateEmailById(@Param("id") Long id, @Param("email") String email);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.password = :password WHERE c.id = :id")
    void updatePasswordById(@Param("id") Long id, @Param("password") String password);

    @Transactional @Modifying
    @Query("UPDATE User c SET c.updatedAt = :updatedAt WHERE c.id = :id")
    void setUpdatedAtById(@Param("id") Long id, @Param("updatedAt") Instant updatedAt);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}