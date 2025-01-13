package com.Ecommence.backend.service;

import com.Ecommence.backend.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto); // 创建或更新产品
    List<ProductDto> getAllProducts(); // 获取所有产品
    ProductDto getProductById(Long id); // 根据 ID 获取产品
    void deleteProduct(Long id); // 删除产品
    ProductDto updateProduct(ProductDto productDto);
}
