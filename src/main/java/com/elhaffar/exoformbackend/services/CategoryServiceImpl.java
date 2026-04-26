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
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
