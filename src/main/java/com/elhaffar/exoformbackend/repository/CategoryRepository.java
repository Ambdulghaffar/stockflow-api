package com.elhaffar.exoformbackend.repository;

import com.elhaffar.exoformbackend.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    // Pagination sans filtre
    Page<Category> findAll(Pageable pageable);

    // Recherche sur le nom ou la description
    @Query("SELECT c FROM Category c WHERE " +
           "LOWER(c.name)        LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Category> searchCategories(@Param("search") String search, Pageable pageable);
}
