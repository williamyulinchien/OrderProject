package com.Ecommence.backend.service;

import com.Ecommence.backend.dto.ProductDto;
import com.Ecommence.backend.entity.Product;
import com.Ecommence.backend.exception.ProductException;
import com.Ecommence.backend.mapper.ProductMapper;
import com.Ecommence.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        // check the exist product
        if (productRepository.existsByName(productDto.getProductName())) {
            throw new ProductException("Product already exists with name: " + productDto.getProductName());
        }
        // valid the rquest body
        validateProductData(productDto);
        Product product = ProductMapper.mapToProduct(productDto);
        // image url check
        if (productDto.getProductImageUrl() != null && !productDto.getProductImageUrl().isEmpty()) {
            product.setProductImageUrl(productDto.getProductImageUrl());
        } else {
            product.setProductImageUrl("https://example.com/default_product.png");
        }
        // save to database
        Product savedProduct = productRepository.save(product);
        // Return to ProductDto
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        // check if there is the product
        Product product = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new ProductException("Product not found with ID: " + productDto.getProductId()));
        // check if there is a duplicate product name
        if (productDto.getProductName() != null && !productDto.getProductName().equals(product.getName())) {
            if (productRepository.existsByName(productDto.getProductName())) {
                throw new ProductException("Product already exists with name: " + productDto.getProductName());
            }
            product.setName(productDto.getProductName());
        }
        // valid the rquest body
        validateProductData(productDto);
        // Update the product
       product.setPrice(productDto.getPrice());
       product.setDescription(productDto.getDescription());
       product.setQuantity(productDto.getQuantity());
        if (productDto.getProductImageUrl() != null && !productDto.getProductImageUrl().isEmpty()) {
            product.setProductImageUrl(productDto.getProductImageUrl());
        } else {
            product.setProductImageUrl("https://example.com/default_product.png");
        }
        //save to database
        Product updatedProduct = productRepository.save(product);
        // Return to ProductDto
        return ProductMapper.mapToProductDto(updatedProduct);
    }
    // method: to get all products
    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }
    // method: to  get a specific product
    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found with id: " + id));
        return ProductMapper.mapToProductDto(product);
    }
    //  // method: to  delete a specific product
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // product validation
    private void validateProductData(ProductDto productDto) {
        if (productDto.getPrice() == null || productDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        if (productDto.getQuantity() == null || productDto.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }
    }
}
