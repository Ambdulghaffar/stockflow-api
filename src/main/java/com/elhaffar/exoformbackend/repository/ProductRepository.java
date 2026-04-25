package com.elhaffar.exoformbackend.repository;

import com.elhaffar.exoformbackend.entities.Product;
import com.elhaffar.exoformbackend.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Filtre par statut
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    // Filtre par catégorie
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    // Filtre par statut ET catégorie
    Page<Product> findByStatusAndCategoryId(ProductStatus status, Integer categoryId, Pageable pageable);

    // Recherche sur nom ou description
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name)        LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> searchProducts(@Param("search") String search, Pageable pageable);

    // Stats
    long countByStatus(ProductStatus status);
    long countByCategoryId(Integer categoryId);
}
