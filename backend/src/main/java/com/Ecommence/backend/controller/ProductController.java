package com.Ecommence.backend.controller;

import com.Ecommence.backend.dto.ProductDto;
import com.Ecommence.backend.dto.UserResponseDto;
import com.Ecommence.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    // DI dependency
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    //1. Create a Product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.createProduct(productDto); // 單獨處理新增邏輯
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    //2. Get all products
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    // 3. Get a specific product base on productId
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    // 4. Update a specific product base on productId
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @PathVariable Long id, @RequestBody ProductDto productDto) {
        productDto.setProductId(id);
        ProductDto updatedProduct = productService.updateProduct(productDto); // 單獨處理更新邏輯
        return ResponseEntity.ok(updatedProduct);
    }

    // 5. Delete a specific product base on productId
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product delete successfully!");
    }
}
