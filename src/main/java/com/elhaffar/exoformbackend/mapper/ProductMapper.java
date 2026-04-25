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
