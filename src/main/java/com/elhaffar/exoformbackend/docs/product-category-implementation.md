# Guide d'implémentation — Modules Product & Category
> Fichier destiné à GitHub Copilot pour générer le code complet du backend Spring Boot.
> Suivre exactement le même pattern que le module `User` déjà implémenté.

---

## Contexte du projet

- **Framework** : Spring Boot
- **Package racine** : `com.elhaffar.exoformbackend`
- **ORM** : JPA / Hibernate
- **Mapper** : MapStruct (`componentModel = "spring"`)
- **Sécurité** : `@PreAuthorize("hasAuthority('ROLE_ADMIN')")`
- **Gestion d'erreurs** : `ResourceNotFoundException` et `BusinessException` dans `exceptions/`
- **Pagination** : `PageResponseDTO<T>` générique déjà existant dans `dto/common/`
- **Pattern** : exactement le même que `UserServiceImpl`, `UserController`, `UserMapper`

---

## 1. Enums

### `enums/ProductStatus.java`
```java
package com.elhaffar.exoformbackend.enums;

public enum ProductStatus {
    ACTIVE,
    INACTIVE,
    OUT_OF_STOCK
}
```

---

## 2. Entités

### `entities/Category.java`
```java
package com.elhaffar.exoformbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    // Relation : une catégorie a plusieurs produits
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### `entities/Product.java`
```java
package com.elhaffar.exoformbackend.entities;

import com.elhaffar.exoformbackend.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "image_url")
    private String imageUrl;

    // Relation : plusieurs produits appartiennent à une catégorie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

---

## 3. DTOs

### `dto/category/CategoryRequestDTO.java`
```java
package com.elhaffar.exoformbackend.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(

        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
        String name,

        @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
        String description,

        String imageUrl
) {}
```

### `dto/category/CategoryResponseDTO.java`
```java
package com.elhaffar.exoformbackend.dto.category;

import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Integer id,
        String name,
        String description,
        String imageUrl,
        int productCount,       // nombre de produits dans la catégorie
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
```

### `dto/product/ProductRequestDTO.java`
```java
package com.elhaffar.exoformbackend.dto.product;

import com.elhaffar.exoformbackend.enums.ProductStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(

        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 2, max = 200, message = "Le nom doit contenir entre 2 et 200 caractères")
        String name,

        @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
        String description,

        @NotNull(message = "Le prix est obligatoire")
        @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
        BigDecimal price,

        @NotNull(message = "Le stock est obligatoire")
        @Min(value = 0, message = "Le stock ne peut pas être négatif")
        Integer stock,

        String imageUrl,

        @NotNull(message = "La catégorie est obligatoire")
        Integer categoryId,

        @NotNull(message = "Le statut est obligatoire")
        ProductStatus status
) {}
```

### `dto/product/ProductResponseDTO.java`
```java
package com.elhaffar.exoformbackend.dto.product;

import com.elhaffar.exoformbackend.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl,
        Integer categoryId,
        String categoryName,
        ProductStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
```

---

## 4. Repositories

### `repository/CategoryRepository.java`
```java
package com.elhaffar.exoformbackend.repository;

import com.elhaffar.exoformbackend.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
```

### `repository/ProductRepository.java`
```java
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
```

---

## 5. Mappers

### `mapper/CategoryMapper.java`
```java
package com.elhaffar.exoformbackend.mapper;

import com.elhaffar.exoformbackend.dto.category.CategoryRequestDTO;
import com.elhaffar.exoformbackend.dto.category.CategoryResponseDTO;
import com.elhaffar.exoformbackend.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // productCount = taille de la liste products
    @Mapping(target = "productCount", expression = "java(category.getProducts().size())")
    CategoryResponseDTO toResponseDTO(Category category);

    List<CategoryResponseDTO> toResponseDTOList(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCategoryFromDto(CategoryRequestDTO dto, @MappingTarget Category category);
}
```

### `mapper/ProductMapper.java`
```java
package com.elhaffar.exoformbackend.mapper;

import com.elhaffar.exoformbackend.dto.product.ProductRequestDTO;
import com.elhaffar.exoformbackend.dto.product.ProductResponseDTO;
import com.elhaffar.exoformbackend.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // MapStruct mappe automatiquement category.id → categoryId et category.name → categoryName
    @Mapping(target = "categoryId",   source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponseDTO toResponseDTO(Product product);

    List<ProductResponseDTO> toResponseDTOList(List<Product> products);

    @Mapping(target = "id",        ignore = true)
    @Mapping(target = "category",  ignore = true) // résolu manuellement dans le service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequestDTO dto);

    @Mapping(target = "id",        ignore = true)
    @Mapping(target = "category",  ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProductFromDto(ProductRequestDTO dto, @MappingTarget Product product);
}
```

---

## 6. Services

### `services/CategoryService.java` (interface)
```java
package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.category.CategoryRequestDTO;
import com.elhaffar.exoformbackend.dto.category.CategoryResponseDTO;
import com.elhaffar.exoformbackend.dto.common.PageResponseDTO;

public interface CategoryService {
    PageResponseDTO<CategoryResponseDTO> getAllCategories(int page, int size, String sortBy, String sortDir, String search);
    CategoryResponseDTO getCategoryById(Integer id);
    CategoryResponseDTO createCategory(CategoryRequestDTO dto);
    CategoryResponseDTO updateCategory(Integer id, CategoryRequestDTO dto);
    void deleteCategory(Integer id);
}
```

### `services/CategoryServiceImpl.java`
```java
package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.category.CategoryRequestDTO;
import com.elhaffar.exoformbackend.dto.category.CategoryResponseDTO;
import com.elhaffar.exoformbackend.dto.common.PageResponseDTO;
import com.elhaffar.exoformbackend.entities.Category;
import com.elhaffar.exoformbackend.exceptions.BusinessException;
import com.elhaffar.exoformbackend.exceptions.ResourceNotFoundException;
import com.elhaffar.exoformbackend.mapper.CategoryMapper;
import com.elhaffar.exoformbackend.repository.CategoryRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public PageResponseDTO<CategoryResponseDTO> getAllCategories(
            int page, int size, String sortBy, String sortDir, String search) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        boolean hasSearch = search != null && !search.isBlank();

        Page<Category> result = hasSearch
                ? categoryRepository.searchCategories(search, pageable)
                : categoryRepository.findAll(pageable);

        return PageResponseDTO.from(result.map(categoryMapper::toResponseDTO));
    }

    @Override
    public CategoryResponseDTO getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie", id));
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        if (categoryRepository.findByName(dto.name()).isPresent()) {
            throw new BusinessException("Une catégorie avec ce nom existe déjà");
        }
        Category saved = categoryRepository.save(categoryMapper.toEntity(dto));
        return categoryMapper.toResponseDTO(saved);
    }

    @Override
    public CategoryResponseDTO updateCategory(Integer id, CategoryRequestDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie", id));

        categoryRepository.findByName(dto.name())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new BusinessException("Une catégorie avec ce nom existe déjà"); });

        categoryMapper.updateCategoryFromDto(dto, existing);
        return categoryMapper.toResponseDTO(categoryRepository.save(existing));
    }

    @Override
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Catégorie", id);
        }
        categoryRepository.deleteById(id);
    }
}
```

### `services/ProductService.java` (interface)
```java
package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.common.PageResponseDTO;
import com.elhaffar.exoformbackend.dto.product.ProductRequestDTO;
import com.elhaffar.exoformbackend.dto.product.ProductResponseDTO;

public interface ProductService {
    PageResponseDTO<ProductResponseDTO> getAllProducts(int page, int size, String sortBy, String sortDir, String status, Integer categoryId, String search);
    ProductResponseDTO getProductById(Integer id);
    ProductResponseDTO createProduct(ProductRequestDTO dto);
    ProductResponseDTO updateProduct(Integer id, ProductRequestDTO dto);
    void deleteProduct(Integer id);
}
```

### `services/ProductServiceImpl.java`
```java
package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.common.PageResponseDTO;
import com.elhaffar.exoformbackend.dto.product.ProductRequestDTO;
import com.elhaffar.exoformbackend.dto.product.ProductResponseDTO;
import com.elhaffar.exoformbackend.entities.Category;
import com.elhaffar.exoformbackend.entities.Product;
import com.elhaffar.exoformbackend.enums.ProductStatus;
import com.elhaffar.exoformbackend.exceptions.BusinessException;
import com.elhaffar.exoformbackend.exceptions.ResourceNotFoundException;
import com.elhaffar.exoformbackend.mapper.ProductMapper;
import com.elhaffar.exoformbackend.repository.CategoryRepository;
import com.elhaffar.exoformbackend.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                               CategoryRepository categoryRepository,
                               ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public PageResponseDTO<ProductResponseDTO> getAllProducts(
            int page, int size, String sortBy, String sortDir,
            String status, Integer categoryId, String search) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        boolean hasSearch     = search     != null && !search.isBlank();
        boolean hasStatus     = status     != null && !status.isBlank() && !status.equalsIgnoreCase("all");
        boolean hasCategoryId = categoryId != null;

        Page<Product> result;

        if (hasSearch) {
            // Recherche texte — priorité sur les autres filtres
            result = productRepository.searchProducts(search, pageable);
        } else if (hasStatus && hasCategoryId) {
            try {
                ProductStatus productStatus = ProductStatus.valueOf(status.toUpperCase());
                result = productRepository.findByStatusAndCategoryId(productStatus, categoryId, pageable);
            } catch (IllegalArgumentException e) {
                result = productRepository.findByCategoryId(categoryId, pageable);
            }
        } else if (hasStatus) {
            try {
                ProductStatus productStatus = ProductStatus.valueOf(status.toUpperCase());
                result = productRepository.findByStatus(productStatus, pageable);
            } catch (IllegalArgumentException e) {
                result = productRepository.findAll(pageable);
            }
        } else if (hasCategoryId) {
            result = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            result = productRepository.findAll(pageable);
        }

        return PageResponseDTO.from(result.map(productMapper::toResponseDTO));
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        return productRepository.findById(id)
                .map(productMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", id));
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        // Vérifie que la catégorie existe
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie", dto.categoryId()));

        Product product = productMapper.toEntity(dto);
        product.setCategory(category); // résolution manuelle de la relation

        return productMapper.toResponseDTO(productRepository.save(product));
    }

    @Override
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", id));

        // Vérifie que la nouvelle catégorie existe
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie", dto.categoryId()));

        productMapper.updateProductFromDto(dto, existing);
        existing.setCategory(category);

        return productMapper.toResponseDTO(productRepository.save(existing));
    }

    @Override
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produit", id);
        }
        productRepository.deleteById(id);
    }
}
```

---

## 7. Controllers

### `controllers/CategoryController.java`
```java
package com.elhaffar.exoformbackend.controllers;

import com.elhaffar.exoformbackend.dto.category.CategoryRequestDTO;
import com.elhaffar.exoformbackend.dto.category.CategoryResponseDTO;
import com.elhaffar.exoformbackend.dto.common.PageResponseDTO;
import com.elhaffar.exoformbackend.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<CategoryResponseDTO>> getAllCategories(
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "10")   int size,
            @RequestParam(defaultValue = "id")   String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false)      String search
    ) {
        return ResponseEntity.ok(
            categoryService.getAllCategories(page, size, sortBy, sortDir, search)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
```

### `controllers/ProductController.java`
```java
package com.elhaffar.exoformbackend.controllers;

import com.elhaffar.exoformbackend.dto.common.PageResponseDTO;
import com.elhaffar.exoformbackend.dto.product.ProductRequestDTO;
import com.elhaffar.exoformbackend.dto.product.ProductResponseDTO;
import com.elhaffar.exoformbackend.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "10")   int size,
            @RequestParam(defaultValue = "id")   String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false)      String status,
            @RequestParam(required = false)      Integer categoryId,
            @RequestParam(required = false)      String search
    ) {
        return ResponseEntity.ok(
            productService.getAllProducts(page, size, sortBy, sortDir, status, categoryId, search)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## 8. Endpoints API — Récapitulatif

### Categories — `/api/categories`
| Méthode | URL | Description | HTTP |
|---------|-----|-------------|------|
| GET | `/api/categories?page=0&size=10&sortBy=id&sortDir=desc&search=` | Liste paginée + recherche | 200 |
| GET | `/api/categories/{id}` | Détail d'une catégorie | 200 |
| POST | `/api/categories` | Créer une catégorie | 201 |
| PUT | `/api/categories/{id}` | Modifier une catégorie | 200 |
| DELETE | `/api/categories/{id}` | Supprimer une catégorie | 204 |

### Products — `/api/products`
| Méthode | URL | Description | HTTP |
|---------|-----|-------------|------|
| GET | `/api/products?page=0&size=10&sortBy=id&sortDir=desc&status=ACTIVE&categoryId=1&search=` | Liste paginée + filtres | 200 |
| GET | `/api/products/{id}` | Détail d'un produit | 200 |
| POST | `/api/products` | Créer un produit | 201 |
| PUT | `/api/products/{id}` | Modifier un produit | 200 |
| DELETE | `/api/products/{id}` | Supprimer un produit | 204 |

---

## 9. Réponses d'erreur — GlobalExceptionHandler (déjà existant)

Les cas suivants sont déjà gérés par `GlobalExceptionHandler` :

| Exception | HTTP | Cas |
|-----------|------|-----|
| `ResourceNotFoundException` | 404 | Produit/Catégorie introuvable |
| `BusinessException` | 409 | Nom de catégorie déjà existant |
| `MethodArgumentNotValidException` | 400 | Validation `@Valid` échouée |
| `Exception` | 500 | Erreur inattendue |

---

## 10. Notes importantes pour Copilot

1. **Ne pas modifier** `GlobalExceptionHandler`, `ResourceNotFoundException`, `BusinessException`, `PageResponseDTO` — ils sont déjà implémentés et fonctionnels.
2. **Ne pas modifier** `UserMapper`, `UserService`, `UserController` — ne rien toucher au module User.
3. **Ajouter les imports** `@Query` et `@Param` dans les repositories.
4. **`category` dans `ProductMapper`** est ignoré intentionnellement — il est résolu manuellement dans `ProductServiceImpl` via `categoryRepository.findById()`.
5. **`productCount` dans `CategoryMapper`** utilise une expression Java MapStruct — s'assurer que `FetchType.LAZY` ne cause pas de `LazyInitializationException` en ajoutant `@Transactional` sur les méthodes de service si nécessaire.
6. Le pattern de pagination est identique à `UserService` — réutiliser `PageResponseDTO.from(page.map(...))`.
