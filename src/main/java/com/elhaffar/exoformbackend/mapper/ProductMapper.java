package com.elhaffar.exoformbackend.mapper;

import com.elhaffar.exoformbackend.dto.product.ProductRequestDTO;
import com.elhaffar.exoformbackend.dto.product.ProductResponseDTO;
import com.elhaffar.exoformbackend.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // 1. Transformation Unique (Détails d'un produit)
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponseDTO toResponseDTO(Product product);

    // 2. Transformation de Liste (Pour le "Afficher tout")
    List<ProductResponseDTO> toResponseDTOList(List<Product> products);

    // 3. Création (DTO -> Nouvelle Entité)
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequestDTO dto);

}
