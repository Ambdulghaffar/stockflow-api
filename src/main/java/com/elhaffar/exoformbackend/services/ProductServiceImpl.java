package com.elhaffar.exoformbackend.services;

import com.elhaffar.exoformbackend.dto.product.ProductRequestDTO;
import com.elhaffar.exoformbackend.dto.product.ProductResponseDTO;
import com.elhaffar.exoformbackend.entities.Category;
import com.elhaffar.exoformbackend.entities.Product;
import com.elhaffar.exoformbackend.mapper.ProductMapper;
import com.elhaffar.exoformbackend.repository.CategoryRepository;
import com.elhaffar.exoformbackend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseDTOList(products);
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(productRequestDTO.categoryId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id : " + productRequestDTO.categoryId()));
        Product productToSave = productMapper.toEntity(productRequestDTO);
        productToSave.setCategory(category);
        Product savedProduct = productRepository.save(productToSave);
        return productMapper.toResponseDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO productRequestDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + id));

        Category category = categoryRepository.findById(productRequestDTO.categoryId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id : " + productRequestDTO.categoryId()));

        productMapper.updateProductFromDto(productRequestDTO, existingProduct);
        existingProduct.setCategory(category);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id) {
        if(!productRepository.existsById(id)) {
            throw new RuntimeException("Produit non trouvé avec l'id : " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + id));
        return productMapper.toResponseDTO(product);
    }


}
