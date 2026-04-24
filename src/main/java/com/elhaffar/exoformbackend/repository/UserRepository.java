package com.elhaffar.exoformbackend.repository;

import com.elhaffar.exoformbackend.entities.User;
import com.elhaffar.exoformbackend.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Page<User> findAll(Pageable pageable);
    Page<User> findByRole(UserRole role, Pageable pageable);
    long countByRole(UserRole role);

    // Recherche sur username, email ou phone en une seule requête SQL
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email)    LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "u.phone           LIKE CONCAT('%', :search, '%')")
    Page<User> searchUsers(@Param("search") String search, Pageable pageable);
}
