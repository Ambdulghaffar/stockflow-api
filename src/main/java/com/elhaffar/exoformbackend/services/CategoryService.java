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
