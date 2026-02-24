package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.category.CategoryRequestDTO;
import com.elhaffar.exoformbackend.dto.category.CategoryResponseDTO;
import com.elhaffar.exoformbackend.entities.Category;
import com.elhaffar.exoformbackend.mapper.CategoryMapper;
import com.elhaffar.exoformbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByIdDesc();
        return categoryMapper.toResponseDTOList(categories);
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRepository.findByName(categoryRequestDTO.name()).isPresent()) {
            throw new RuntimeException("Ce nom de catégorie existe déjà !");
        }
        Category categoryToSave = categoryMapper.toEntity(categoryRequestDTO);
        Category savedCategory = categoryRepository.save(categoryToSave);
        return categoryMapper.toResponseDTO(savedCategory);
    }

    @Override
    public CategoryResponseDTO updateCategory(Integer id, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id : " + id));

        categoryRepository.findByName(categoryRequestDTO.name()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new RuntimeException("Ce nom de catégorie existe déjà !");
            }
        });
        categoryMapper.updateCategoryFromDto(categoryRequestDTO, existingCategory);
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toResponseDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Catégorie non trouvée avec l'id : " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDTO getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id : " + id));
        return categoryMapper.toResponseDTO(category);
    }
}
