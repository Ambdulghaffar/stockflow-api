package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.product.ProductRequestDTO;
import com.elhaffar.exoformbackend.dto.product.ProductResponseDTO;
import com.elhaffar.exoformbackend.entities.Product;
import com.elhaffar.exoformbackend.mapper.ProductMapper;
import com.elhaffar.exoformbackend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseDTOList(products);
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        // 1. Vérifier si la catégorie existe en base de données
        /*Category category = categoryRepository.findById(productRequestDTO.categoryId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id : " + productRequestDTO.categoryId()));

         */

        // 2. Mapper le DTO en Entité Product
        Product productToSave = productMapper.toEntity(productRequestDTO);

        // 3. Lier l'entité catégorie récupérée au produit
        //productToSave.setCategory(category);

        // 4. Sauvegarder dans la DB
        Product savedProduct = productRepository.save(productToSave);

        // 5. Retourner le ResponseDTO
        return productMapper.toResponseDTO(savedProduct);
    }


}
