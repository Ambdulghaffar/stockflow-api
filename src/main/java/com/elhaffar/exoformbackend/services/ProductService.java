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
