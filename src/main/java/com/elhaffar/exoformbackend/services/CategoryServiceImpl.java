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
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toResponseDTOList(categories);
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        // 1. Mapper le DTO en Entité Category
        Category categoryToSave = categoryMapper.toEntity(categoryRequestDTO);

        // 2. Sauvegarder dans la DB
        Category savedCategory = categoryRepository.save(categoryToSave);

        // 3. Retourner le ResponseDTO
        return categoryMapper.toResponseDTO(savedCategory);
    }

    @Override
    public CategoryResponseDTO updateCategory(Integer id, CategoryRequestDTO categoryRequestDTO) {
        // 1. Récupérer la catégorie existante
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id : " + id));

        // 2. Mettre à jour les champs de l'entité existante avec les données du DTO
        categoryMapper.updateCategoryFromDto(categoryRequestDTO, existingCategory);

        // 3. Sauvegarder les modifications dans la DB
        Category updatedCategory = categoryRepository.save(existingCategory);

        // 4. Retourner le ResponseDTO mis à jour
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
