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

    // 1. Transformation Unique (Détails d'une catégorie)
    CategoryResponseDTO toResponseDTO(Category category);

    // 2. Transformation de Liste (Pour le "Afficher tout")
    List<CategoryResponseDTO> toResponseDTOList(List<Category> categories);

    // 3. Création (DTO -> Nouvelle Entité)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryRequestDTO dto);

    /**
     * 4. Mise à jour (DTO -> Entité existante)
     * Met à jour l'entité existante avec les données du DTO
     * @MappingTarget indique à MapStruct de modifier l'objet existant
     * au lieu d'en créer un nouveau.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCategoryFromDto(CategoryRequestDTO dto, @MappingTarget Category category);

}
