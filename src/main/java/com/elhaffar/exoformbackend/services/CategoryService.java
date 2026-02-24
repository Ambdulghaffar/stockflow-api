package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.category.CategoryRequestDTO;
import com.elhaffar.exoformbackend.dto.category.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO updateCategory(Integer id, CategoryRequestDTO categoryRequestDTO);
    void deleteCategory(Integer id);
    CategoryResponseDTO getCategoryById(Integer id);
}
