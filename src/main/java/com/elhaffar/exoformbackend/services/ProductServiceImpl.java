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

    @Override
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO productRequestDTO) {
        // 1. Récupérer le produit existant
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + id));

        // 2. Vérifier si la catégorie existe en base de données
        /*Category category = categoryRepository.findById(productRequestDTO.categoryId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id : " + productRequestDTO.categoryId()));

         */

        // 3. Mettre à jour les champs de l'entité existante avec les données du DTO
        productMapper.updateProductFromDto(productRequestDTO, existingProduct);

        // 4. Lier l'entité catégorie récupérée au produit
        //existingProduct.setCategory(category);

        // 5. Sauvegarder les modifications dans la DB
        Product updatedProduct = productRepository.save(existingProduct);

        // 6. Retourner le ResponseDTO mis à jour
        return productMapper.toResponseDTO(updatedProduct);
    }


}
