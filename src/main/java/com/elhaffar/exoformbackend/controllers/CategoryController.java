package com.elhaffar.exoformbackend.controllers;

import com.elhaffar.exoformbackend.dto.category.CategoryRequestDTO;
import com.elhaffar.exoformbackend.dto.category.CategoryResponseDTO;
import com.elhaffar.exoformbackend.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public CategoryResponseDTO createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return categoryService.createCategory(categoryRequestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public CategoryResponseDTO updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return categoryService.updateCategory(id, categoryRequestDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }
}
