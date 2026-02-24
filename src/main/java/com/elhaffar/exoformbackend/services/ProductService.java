package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.product.ProductRequestDTO;
import com.elhaffar.exoformbackend.dto.product.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    // On reçoit les données brutes, on renvoie l'objet créé avec son ID et ses dates
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO updateProduct(Integer id, ProductRequestDTO productRequestDTO);
    void deleteProduct(Integer id);
    ProductResponseDTO getProductById(Integer id);

}
