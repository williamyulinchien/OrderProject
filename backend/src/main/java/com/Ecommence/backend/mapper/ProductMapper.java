package com.Ecommence.backend.mapper;

import com.Ecommence.backend.dto.ProductDto;
import com.Ecommence.backend.entity.Product;

public class ProductMapper {

    // Product  entity-> ProductDto
    public static ProductDto mapToProductDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getQuantity());
        dto.setProductImageUrl(product.getProductImageUrl());
        return dto;
    }

    // ProductDto -> Product  entity
    public static Product mapToProduct(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productDto.getProductId()); // 更新操作需要 ID
        product.setName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());
        if(productDto.getProductImageUrl()!=null && productDto.getProductImageUrl().isEmpty()){
            product.setProductImageUrl(productDto.getProductImageUrl());
        }
        return product;
    }
}
